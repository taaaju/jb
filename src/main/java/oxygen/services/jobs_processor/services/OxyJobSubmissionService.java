package oxygen.services.jobs_processor.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oxygen.services.jobs_processor.config.OxyJobRetryConfigProperties;
import oxygen.services.jobs_processor.models.EventStatus;
import oxygen.services.jobs_processor.models.JobRequest;
import oxygen.services.jobs_processor.persistence.entities.OrchestratorEvent;
import oxygen.services.jobs_processor.persistence.repositories.OrchestratorEventRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OxyJobSubmissionService {
    private final OxyJobRetryConfigProperties oxyJobRetryConfigProperties;
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

        repository.save(orchestratorEvent);
        return orchestratorEvent;
    }
}