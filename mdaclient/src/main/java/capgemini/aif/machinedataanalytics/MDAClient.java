package capgemini.aif.machinedataanalytics;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import com.microsoft.azure.eventhubs.PartitionSender;

import capgemini.aif.machinedataanalytics.service.FFTDetailsRepository;
import capgemini.aif.machinedataanalytics.service.MetadataRepository;
import capgemini.aif.machinedataanalytics.service.ReelRepository;
import capgemini.aif.machinedataanalytics.service.Telemetry;
import capgemini.aif.machinedataanalytics.service.Telemetry.SendStatus;
import capgemini.aif.machinedataanalytics.service.TelemetryRepository;
import capgemini.aif.machinedataanalytics.service.WorkOrderRepository;

@Configuration
@PropertySource("classpath:azure.config.properties")
@SpringBootApplication
public class MDAClient {

	private static final Logger log = LoggerFactory.getLogger(MDAClient.class);

	public static void main(String[] args) {
		// here is an anti-pattern, but it's OK ;-)
		try {
			if(args.length != 1) {
				throw new RuntimeException("Expect a Reel Identifier as argument");
			}
			SpringApplication.run(MDAClient.class, args);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Value("${eventhub.namespace.name}")
	private String NamespaceName;

	@Value("${eventhub.name}")
	private String EventHubName;

	@Value("${shared.key.name}")
	private String SharedAccessSignatureKeyName;

	@Value("${shared.key}")
	private String SharedAccessSignatureKey;

	@Value("${client.sleep.milliseconds}")
	private int ClientSleepMillis;
	
	private ConnectionStringBuilder connStr;

	@Autowired private TelemetryRepository tele_r;
	
	/* not used yet -- don't want the overhead */
//	@Autowired private WorkOrderRepository wo_r;
//	@Autowired private ReelRepository reel_r;
//	@Autowired private MetadataRepository meta_r;
//	@Autowired private FFTDetailsRepository fft_r;
		
	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			ArrayList<Telemetry> l = new ArrayList<Telemetry>(tele_r.findAllByReelidentifierAndSendstatus((args[0]),SendStatus.NEW));
			if(l.size() > 0) {
				log.info("Found new messages to Send: "+l.size());
				log.info("Pausing between sends for milliseconds: "+ClientSleepMillis);
				log.debug(NamespaceName+" "+EventHubName+" "+SharedAccessSignatureKeyName+" "+SharedAccessSignatureKey);
				connStr  = new ConnectionStringBuilder()
					    .setNamespaceName(NamespaceName)// to target National clouds - use .setEndpoint(URI)
					    .setEventHubName(EventHubName)
					    .setSasKeyName(SharedAccessSignatureKeyName)
					    .setSasKey(SharedAccessSignatureKey);
				for(Telemetry t : l) {                                   // FOR EACH T RECORD
					if(t.getSendstatus() == null                         // IF STATUS IS NOT SENT
							|| !t.getSendstatus().equals(SendStatus.SENT)) {
						// we only want to pause if we attempt to send
						TimeUnit.MILLISECONDS.sleep(ClientSleepMillis);  // PAUSE
						t.setTimestamp(Timestamp.valueOf(LocalDateTime.now().format(formatter)));
						tele_r.save(t);                                  // SAVE TO DB
						if(send(t)) {                                    // SEND TO EVENTHUB
							t.setSendstatus(SendStatus.SENT);
							tele_r.save(t);
						} else {
							t.setSendstatus(SendStatus.FAILED);
							tele_r.save(t);
							log.error("Sending failed for telemetry: "+new GsonBuilder().create().toJson(t));
						}
					}
				}
				System.exit(0);
			} else {
				throw new RuntimeException("No telemetry found for reel");
			}
		};
	}

	public Boolean send(Telemetry telemetry) {
		log.debug("Entering send EventData "+telemetry.toString());

        final Gson gson = new GsonBuilder().create();
        byte[] payloadBytes = gson.toJson(telemetry).getBytes(Charset.defaultCharset());
        final EventData event = EventData.create(payloadBytes);
        log.debug("sending json to eventhub "+gson.toJson(telemetry));
        
        try {
        	send(event);
        	log.info("\nTelemetry Sent: \n\ttimestamp:"
        			+telemetry.getTimestamp()+"\n\treelidentifier:"
        			+telemetry.getReelidentifier());
        } catch (Exception e) {
        	e.printStackTrace();
    		return Boolean.FALSE;
        }
		return Boolean.TRUE;
	}

	private void send(EventData event)
            throws EventHubException, ExecutionException, InterruptedException, IOException {
		
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