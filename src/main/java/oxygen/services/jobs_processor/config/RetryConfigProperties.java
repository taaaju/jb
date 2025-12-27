package oxygen.services.jobs_processor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "orchestrator.retry")
@Getter
@Setter
public class RetryConfigProperties {

    private BackoffSettings defaultStrategy;
    private Map<String, BackoffSettings> services = new HashMap<>();

    @Getter
    @Setter
    public static class BackoffSettings {
        private int initialDelaySeconds = 5;
        private int multiplier = 2;
        private int maxDelaySeconds = 3600;
        private int retryLimit = 5;
    }
    public BackoffSettings getSettingsFor(String eventType) {
        return services.getOrDefault(eventType, defaultStrategy);
    }
}