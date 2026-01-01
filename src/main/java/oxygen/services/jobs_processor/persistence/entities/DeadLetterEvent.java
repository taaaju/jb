package oxygen.services.jobs_processor.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "dead_letter_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeadLetterEvent {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "event_identifier", nullable = false, unique = true)
    private String eventIdentifier;

    @Column(name = "event_type", nullable = false, length = 255)
    private String eventType;

    @Column(name = "callback_url", nullable = false, columnDefinition = "text")
    private String callbackUrl;

    @Column(name = "status", nullable = false, length = 50) // FAILED | INVALID_PAYLOAD
    private String status;

    @Column(name = "retry_limit", nullable = false)
    private Integer retryLimit = 5;

    @Column(name = "attempts", nullable = false)
    private Integer attempts = 0;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "reason", columnDefinition = "text")
    private String reason;

    @Override
    public String toString() {
        return "DeadLetterEvent{" +
                ", eventIdentifier='" + eventIdentifier +
                ", eventType='" + eventType +
                ", callbackUrl='" + callbackUrl +
                ", status='" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
