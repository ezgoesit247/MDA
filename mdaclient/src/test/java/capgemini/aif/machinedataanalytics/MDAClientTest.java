package capgemini.aif.machinedataanalytics;

import java.io.IOException;
import java.nio.charset.Charset;
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
@PropertySource("classpath:azure.config.test.properties")
@SpringBootApplication
public class MDAClientTest {

	private static final Logger log = LoggerFactory.getLogger(MDAClientTest.class);

	public static void main(String[] args) {
		SpringApplication.run(MDAClientTest.class, args);
	}

	@Value("${test.eventhub.namespace.name}")
	private String NamespaceName;

	@Value("${test.eventhub.name}")
	private String EventHubName;

	@Value("${test.shared.key.name}")
	private String SharedAccessSignatureKeyName;

	@Value("${test.shared.key}")
	private String SharedAccessSignatureKey;

	@Value("${test.client.sleep.milliseconds}")
	private int ClientSleepMillis;
	
		
	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			log.info(NamespaceName);
			log.info(EventHubName);
			log.info(EventHubName);
			log.info(SharedAccessSignatureKey);
			log.info(Integer.toString(ClientSleepMillis));
			
			System.exit(0);
		};
	}

}
