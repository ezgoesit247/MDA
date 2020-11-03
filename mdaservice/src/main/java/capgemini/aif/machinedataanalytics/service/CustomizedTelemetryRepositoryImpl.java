package capgemini.aif.machinedataanalytics.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import com.microsoft.azure.eventhubs.PartitionSender;

import capgemini.aif.machinedataanalytics.service.Telemetry.SendStatus;

@Configuration
@PropertySource("classpath:azure.config.properties")
public class CustomizedTelemetryRepositoryImpl implements CustomizedTelemetryRepository {
	private final Logger log = LoggerFactory.getLogger(CustomizedTelemetryRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

	@Value("${eventhub.namespace.name}")
	private String NamespaceName;

	@Value("${eventhub.name}")
	private String EventHubName;

	@Value("${shared.key.name}")
	private String SharedAccessSignatureKeyName;

	@Value("${shared.key}")
	private String SharedAccessSignatureKey;
	
	@Override
	@Transactional
	public <S extends Telemetry> S save(S entity) {
        S telemetry = null;
        JpaEntityInformation<Telemetry, ?> entityInformation = JpaEntityInformationSupport.getEntityInformation(Telemetry.class, entityManager);

        log.debug("\nSaving JSON:"+entity.toJson()+"\n");
        
        //TODO: Logic to implement an update -- do we actually need it??
        if (entityInformation.isNew(entity)) {

        	if(entity.getVariables() != null
        			&& entity.getVariables().size() > 0) {
	        	for(TelemetryValue tv : entity.getVariables()) {
	        		entityManager.persist(tv);
	        	}
        	} else {
        		throw new RuntimeException("No TelemetryValues set");
        	}
        	
            entityManager.persist(entity);
            telemetry = entity;
        } else {
        	telemetry = entityManager.merge(entity);
        }
//        NULL MEANS WE WANT TO SEND
//        DO NOT SEND IF NOSEND OR IF SENT
        if(telemetry.getSendstatus() == null
        		|| (telemetry.getSendstatus() != SendStatus.NOSEND
        		&& telemetry.getSendstatus() != SendStatus.SENT)) {
	        try {
	        	buildAndSendEventData(telemetry);
	        	telemetry.setSendstatus(SendStatus.SENT);
	        } catch(Exception e) {
	        	telemetry.setSendstatus(SendStatus.FAILED);
	        }
        } else {
        	log.debug("\nNot sending for Reel:"+telemetry.getReelidentifier());
        }
        return telemetry;

	}


	@Override
	public void ehsend(Telemetry telemetry) {
		buildAndSendEventData(telemetry);
	}
	
	private void buildAndSendEventData(Telemetry telemetry) {
		log.debug("\nbuildAndSendEventData:"+telemetry.toString()+"\n");

        final Gson gson = new GsonBuilder().create();
        byte[] payloadBytes = gson.toJson(telemetry).getBytes(Charset.defaultCharset());
        final EventData event = EventData.create(payloadBytes);
        log.debug("\nsending json to eventhub:\n"+gson.toJson(telemetry)+"\n");
        log.info("sending json to eventhub: "+gson.toJson(telemetry));
        
        try {
        	send(event);
        } catch (Exception e) {
        	e.printStackTrace();
    		throw new RuntimeException(e);
        }
	}

	private void send(EventData event)
            throws EventHubException, ExecutionException, InterruptedException, IOException {

		ConnectionStringBuilder connStr  = new ConnectionStringBuilder()
				    .setNamespaceName(NamespaceName)// to target National clouds - use .setEndpoint(URI)
				    .setEventHubName(EventHubName)
				    .setSasKeyName(SharedAccessSignatureKeyName)
				    .setSasKey(SharedAccessSignatureKey);
        // The Executor handles all the asynchronous tasks and this is passed to the EventHubClient.
        // The gives the user control to segregate their thread pool based on the work load.
        // This pool can then be shared across multiple EventHubClient instances.
        // The below sample uses a single thread executor as there is only on EventHubClient instance,
        // handling different flavors of ingestion to Event Hubs here
        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Each EventHubClient instance spins up a new TCP/SSL connection, which is expensive.
        // It is always a best practice to reuse these instances. The following sample shows the same.
        final EventHubClient ehClient = EventHubClient.createSync(connStr.toString(), executorService);;
        PartitionSender sender = null;

        try {
            // senders
            // Type-1 - Send - not tied to any partition
            // EventHubs service will round-robin the events across all EventHubs partitions.
            // This is the recommended & most reliable way to send to EventHubs.
            ehClient.sendSync(event);
        } finally {
            if (sender != null) {
                sender.close()
                        .thenComposeAsync(aVoid -> ehClient.close(), executorService)
                        .whenCompleteAsync((aVoid1, throwable) -> {
                            if (throwable != null) {
                                // wire-up this error to diagnostics infrastructure
//                                System.out.println(String.format("closing failed with error: %s", throwable.toString()));
                            	log.error(String.format("closing failed with error: %s", throwable.toString()));
                            }
                        }, executorService).get();
            } else {
                // This cleans up the resources including any open sockets
                ehClient.closeSync();
            }

            executorService.shutdown();
        }

		log.debug("Leaving send EventData gracefully");
		return;
	}
}
