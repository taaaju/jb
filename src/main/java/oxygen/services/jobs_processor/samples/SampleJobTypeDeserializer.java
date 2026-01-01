package oxygen.services.jobs_processor.samples;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;
import oxygen.services.jobs_processor.models.JobType;

@JsonComponent
public class SampleJobTypeDeserializer extends JsonDeserializer<JobType> {

    @Override
    public JobType deserialize(JsonParser p, DeserializationContext ctxt) {
        return SampleJobType.SAMPLE_PROCESSOR;
    }
}