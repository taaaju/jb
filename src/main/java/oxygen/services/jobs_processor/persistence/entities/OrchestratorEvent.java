package oxygen.services.jobs_processor.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import oxygen.services.jobs_processor.models.EventStatus;

import java.time.Instant;

@Entity
@Table(name = "new_orchestrator_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrchestratorEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_identifier", nullable = false, unique = true)
    private String eventIdentifier;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "callback_url", nullable = false, columnDefinition = "text")
    private String callbackUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private EventStatus status = EventStatus.PENDING;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount = 0;

    @Column(name = "delay", nullable = false, updatable = false)
    private long minimumDelayInSeconds;

    @Column(name = "retry_limit", nullable = false)
    private int retryLimit = 5;

    @Column(name = "next_attempt_at")
    private Instant nextAttemptAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "OrchestratorEvent{" +
                ", eventIdentifier='" + eventIdentifier +
                ", eventType='" + eventType +
                ", callbackUrl='" + callbackUrl +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}

