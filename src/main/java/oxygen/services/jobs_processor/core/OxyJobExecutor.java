package oxygen.services.jobs_processor.core;

import org.springframework.stereotype.Component;
import oxygen.services.jobs_processor.models.JobContext;
import oxygen.services.jobs_processor.models.JobResult;

@Component
public class OxyJobExecutor {

    public JobResult execute(OxyJobHandler handler, JobContext context) {
        try {
            return handler.handle(context);

        } catch (Exception ex) {
            throw new RuntimeException("Job execution failed", ex);
        }
    }
}
