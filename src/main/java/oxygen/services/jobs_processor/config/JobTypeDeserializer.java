package oxygen.services.jobs_processor.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import oxygen.services.jobs_processor.core.JobHandler;
import oxygen.services.jobs_processor.models.JobType;

import java.io.IOException;

@Component
public class JobTypeDeserializer extends JsonDeserializer<JobType> {

    private final ApplicationContext applicationContext;

    public JobTypeDeserializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public JobType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();

        // Find all JobHandler beans and look for a JobType with a matching name
        return applicationContext.getBeansOfType(JobHandler.class).values().stream()
                .map(JobHandler::supports)
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IOException("Unknown JobType: " + value));
    }
}