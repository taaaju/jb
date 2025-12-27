package oxygen.services.jobs_processor.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import oxygen.services.jobs_processor.config.JobTypeDeserializer;

@JsonDeserialize(using = JobTypeDeserializer.class)
public interface JobType {
    String name();
}
