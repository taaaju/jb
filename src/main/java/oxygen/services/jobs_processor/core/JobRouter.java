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
public class JobRouter {
    private final JobExecutor jobExecutor;
    private final Map<JobType, JobHandler> handlers;

    public JobRouter(List<JobHandler> handlerList, JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
        this.handlers = handlerList.stream()
                .collect(Collectors.toUnmodifiableMap(
                        JobHandler::supports,
                        Function.identity(),
                        (a, b) -> {
                            throw new IllegalStateException(
                                    "Duplicate handler for " + a.supports()
                            );
                        }
                ));
    }

    public JobResult route(JobRequest request) {
        JobHandler handler = handlers.get(request.jobType());

        if (handler == null) {
            throw new UnsupportedOperationException(
                    "Unsupported job type: " + request.jobType()
            );
        }

        return jobExecutor.execute(
                handler,
                new JobContext(request.reference(), request.metadata())
        );
    }
}
