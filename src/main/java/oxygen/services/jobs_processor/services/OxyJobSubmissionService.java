package oxygen.services.jobs_processor.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oxygen.services.jobs_processor.config.OxyJobRetryConfigProperties;
import oxygen.services.jobs_processor.models.EventStatus;
import oxygen.services.jobs_processor.models.JobRequest;
import oxygen.services.jobs_processor.models.OrchestratorEvent;
import oxygen.services.jobs_processor.queueing.OxyJobMessengerService;
import oxygen.services.jobs_processor.utils.Utility;


@Slf4j
@Service
@RequiredArgsConstructor
public class OxyJobSubmissionService {
    private final OxyJobMessengerService oxyJobMessengerService;
    private final OxyJobRetryConfigProperties oxyJobRetryConfigProperties;

    public OrchestratorEvent submitNewRequest(JobRequest request, String callBackUrl) {
        var settings = oxyJobRetryConfigProperties.getSettingsFor(request.jobType().name());

        OrchestratorEvent orchestratorEvent = new OrchestratorEvent();
        orchestratorEvent.setEventIdentifier(request.reference());
        orchestratorEvent.setEventType(request.jobType().name());
        orchestratorEvent.setStatus(EventStatus.PENDING.name());
        orchestratorEvent.setCallbackUrl(callBackUrl);

        orchestratorEvent.setMinimumDelayInSeconds(settings.getInitialDelaySeconds());
        orchestratorEvent.setRetryLimit(settings.getRetryLimit());

        oxyJobMessengerService.deliverToOrchestrator(Utility.asSnakeCasedJsonString(orchestratorEvent));

        return orchestratorEvent;
    }
}