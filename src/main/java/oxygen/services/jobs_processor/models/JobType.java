package oxygen.services.jobs_processor.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import oxygen.services.jobs_processor.config.OxyJobTypeDeserializer;

@JsonDeserialize(using = OxyJobTypeDeserializer.class)
public interface JobType {
    String name();
}
