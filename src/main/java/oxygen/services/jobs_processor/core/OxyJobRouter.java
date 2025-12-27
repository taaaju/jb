package oxygen.services.jobs_processor.core;

import org.springframework.stereotype.Component;
import oxygen.services.jobs_processor.models.JobContext;
import oxygen.services.jobs_processor.models.JobRequest;
import oxygen.services.jobs_processor.models.JobResult;
import oxygen.services.jobs_processor.models.JobType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OxyJobRouter {
    private final OxyJobExecutor oxyJobExecutor;
    private final Map<JobType, OxyJobHandler> handlers;

    public OxyJobRouter(List<OxyJobHandler> handlerList, OxyJobExecutor oxyJobExecutor) {
        this.oxyJobExecutor = oxyJobExecutor;
        this.handlers = handlerList.stream()
                .collect(Collectors.toUnmodifiableMap(
                        OxyJobHandler::supports,
                        Function.identity(),
                        (a, b) -> {
                            throw new IllegalStateException(
                                    "Duplicate handler for " + a.supports()
                            );
                        }
                ));
    }

    public JobResult route(JobRequest request) {
        OxyJobHandler handler = handlers.get(request.jobType());

        if (handler == null) {
            throw new UnsupportedOperationException(
                    "Unsupported job type: " + request.jobType()
            );
        }

        return oxyJobExecutor.execute(
                handler,
                new JobContext(request.reference(), request.metadata())
        );
    }
}
