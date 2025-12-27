package oxygen.services.jobs_processor.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import oxygen.services.jobs_processor.core.OxyJobHandler;
import oxygen.services.jobs_processor.models.JobType;

import java.io.IOException;

@Component
public class OxyJobTypeDeserializer extends JsonDeserializer<JobType> {

    private final ApplicationContext applicationContext;

    public OxyJobTypeDeserializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public JobType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();

        // Find all JobHandler beans and look for a JobType with a matching name
        return applicationContext.getBeansOfType(OxyJobHandler.class).values().stream()
                .map(OxyJobHandler::supports)
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IOException("Unknown JobType: " + value));
    }
}