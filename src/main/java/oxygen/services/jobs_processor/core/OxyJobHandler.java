package oxygen.services.jobs_processor.core;

import oxygen.services.jobs_processor.models.JobContext;
import oxygen.services.jobs_processor.models.JobResult;
import oxygen.services.jobs_processor.models.JobType;

public interface OxyJobHandler {

    JobType supports();

    JobResult handle(JobContext jobContext);
}
