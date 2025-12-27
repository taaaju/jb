package oxygen.services.jobs_processor.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oxygen.services.jobs_processor.config.RetryConfigProperties;
import oxygen.services.jobs_processor.models.EventStatus;
import oxygen.services.jobs_processor.models.JobRequest;
import oxygen.services.jobs_processor.persistence.entities.OrchestratorEvent;
import oxygen.services.jobs_processor.persistence.repositories.OrchestratorEventRepository;

@Slf4j
@Service
public class JobSubmissionService {
    private final RetryConfigProperties retryConfigProperties;
    private final OrchestratorEventRepository repository;

    public JobSubmissionService(RetryConfigProperties retryConfigProperties, OrchestratorEventRepository repository) {
        this.retryConfigProperties = retryConfigProperties;
        this.repository = repository;
    }

    public OrchestratorEvent submitNewRequest(JobRequest request, String callBackUrl) {
        var settings = retryConfigProperties.getSettingsFor(request.jobType().name());

        OrchestratorEvent orchestratorEvent = new OrchestratorEvent();
        orchestratorEvent.setEventIdentifier(request.reference());
        orchestratorEvent.setEventType(request.jobType().name());
        orchestratorEvent.setStatus(EventStatus.PENDING);
        orchestratorEvent.setCallbackUrl(callBackUrl);

        orchestratorEvent.setMinimumDelayInSeconds(settings.getInitialDelaySeconds());
        orchestratorEvent.setRetryLimit(settings.getRetryLimit());

        repository.save(orchestratorEvent);
        return orchestratorEvent;
    }
}