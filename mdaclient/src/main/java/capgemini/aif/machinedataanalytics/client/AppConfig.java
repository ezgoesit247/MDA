package capgemini.aif.machinedataanalytics.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;

/* NOT USED 7/19/2018 */
@PropertySource("classpath:azure.config.properties")
public class AppConfig {

	@Value("${eventhub.namespace.name}")
	private String NamespaceName;

	@Value("${eventhub.name}")
	private String EventHubName;

	@Value("${shared.key.name}")
	private String SharedAccessSignatureKeyName;

	@Value("${shared.key}")
	private String SharedAccessSignatureKey;

	@Value("${client.sleep.milliseconds}")
	private String ClientSleepTime;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public ConnectionStringBuilder connectionStringBuilder() { 
		return new ConnectionStringBuilder()
	            .setNamespaceName(NamespaceName)// to target National clouds - use .setEndpoint(URI)
	            .setEventHubName(EventHubName)
	            .setSasKeyName(SharedAccessSignatureKeyName)
	            .setSasKey(SharedAccessSignatureKey);
    }

}
