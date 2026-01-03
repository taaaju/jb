package oxygen.services.jobs_processor.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrchestratorEvent {

    private String eventIdentifier;

    private String eventType;

    private String callbackUrl;

    private String status;

    private int attemptCount = 0;

    private int retryLimit = 5;

    private Instant nextAttemptAt;

    private long minimumDelayInSeconds;

    private long maximumDelayInSeconds;

    @Override
    public String toString() {
        return "OrchestratorEvent{" +
                "eventIdentifier='" + eventIdentifier +
                ", eventType='" + eventType +
                ", callbackUrl='" + callbackUrl +
                ", status='" + status +
                ", maximumDelayInSeconds=" + maximumDelayInSeconds +
                ", minimumDelayInSeconds=" + minimumDelayInSeconds +
                ", attemptCount=" + attemptCount +
                '}';
    }
}
