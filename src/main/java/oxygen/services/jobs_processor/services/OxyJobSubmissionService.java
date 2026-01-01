package oxygen.services.jobs_processor.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import oxygen.services.jobs_processor.config.OxyJobRetryConfigProperties;
import oxygen.services.jobs_processor.models.EventStatus;
import oxygen.services.jobs_processor.models.JobRequest;
import oxygen.services.jobs_processor.persistence.entities.DeadLetterEvent;
import oxygen.services.jobs_processor.persistence.entities.OrchestratorEvent;
import oxygen.services.jobs_processor.persistence.repositories.DeadLetterEventRepository;
import oxygen.services.jobs_processor.persistence.repositories.OrchestratorEventRepository;

import java.time.Instant;

import static oxygen.services.jobs_processor.utils.Utility.asJsonString;

@Slf4j
@Service
@RequiredArgsConstructor
public class OxyJobSubmissionService {
    private final OxyJobRetryConfigProperties oxyJobRetryConfigProperties;
    private final DeadLetterEventRepository deadLetterEventRepository;
    private final OrchestratorEventRepository repository;

    public OrchestratorEvent submitNewRequest(JobRequest request, String callBackUrl) {
        var settings = oxyJobRetryConfigProperties.getSettingsFor(request.jobType().name());

        OrchestratorEvent orchestratorEvent = new OrchestratorEvent();
        orchestratorEvent.setEventIdentifier(request.reference());
        orchestratorEvent.setEventType(request.jobType().name());
        orchestratorEvent.setStatus(EventStatus.PENDING);
        orchestratorEvent.setCallbackUrl(callBackUrl);

        orchestratorEvent.setMinimumDelayInSeconds(settings.getInitialDelaySeconds());
        orchestratorEvent.setRetryLimit(settings.getRetryLimit());
        orchestratorEvent.setCreatedAt(Instant.now());

        try {
            repository.save(orchestratorEvent);
        } catch (Exception exception) {
            boolean messageExists = alreadyExistsError(exception.getMessage());

            if (messageExists) {
                String reason = String.format("Duplicate event caught. Saving record in DLQ for further processing for request with reference %s and context %s. %s", request.reference(), request.metadata(), asJsonString(orchestratorEvent));

                log.info(reason);

                saveDeadLetterRecord(request, orchestratorEvent, reason);

                return orchestratorEvent;
            }

            log.error("Exception saving record with reference {} and context {} {} {}", request.reference(), request.metadata(), orchestratorEvent, exception.getMessage());

            throw exception;
        }

        return orchestratorEvent;
    }

    private void saveDeadLetterRecord(JobRequest request, OrchestratorEvent orchestratorEvent, String reason) {
        DeadLetterEvent deadLetterEvent = new DeadLetterEvent();
        deadLetterEvent.setEventIdentifier(request.reference());
        deadLetterEvent.setEventType(request.jobType().name());
        deadLetterEvent.setStatus(EventStatus.PENDING.name());
        deadLetterEvent.setCallbackUrl(orchestratorEvent.getCallbackUrl());

        deadLetterEvent.setRetryLimit(orchestratorEvent.getRetryLimit());
        deadLetterEvent.setCreatedAt(Instant.now());
        deadLetterEvent.setReason(reason);
        deadLetterEventRepository.save(deadLetterEvent);
    }

    private boolean alreadyExistsError(String message) {
        if (StringUtils.hasText(message)) {
            return message.contains("event_identifier") && message.contains("exists");
        }
        return false;
    }
}