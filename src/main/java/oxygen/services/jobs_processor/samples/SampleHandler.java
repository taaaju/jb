package oxygen.services.jobs_processor.samples;

import oxygen.services.jobs_processor.annotation.HandlerProcessor;
import oxygen.services.jobs_processor.core.OxyJobHandler;
import oxygen.services.jobs_processor.models.JobContext;
import oxygen.services.jobs_processor.models.JobResult;
import oxygen.services.jobs_processor.models.JobState;
import oxygen.services.jobs_processor.models.JobType;

@HandlerProcessor(processorName = "SAMPLE_PROCESSOR")
public class SampleHandler implements OxyJobHandler {

    public static final String IGNORED = "ignored";

    @Override
    public JobType supports() {
        return SampleJobType.SAMPLE_PROCESSOR;
    }

    @Override
    public JobResult handle(JobContext jobContext) {
        return new JobResult(JobState.IGNORED, false, IGNORED);
    }
}
