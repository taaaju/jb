package oxygen.services.jobs_processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import oxygen.services.jobs_processor.models.JobRequest;
import oxygen.services.jobs_processor.models.JobType;
import oxygen.services.jobs_processor.samples.SampleJobTypeDeserializer;

@ExtendWith(MockitoExtension.class)
public class JobProcessorConversionTests {

    private ObjectMapper objectMapper = objectMapper();

    private static final String RETRY_TESTS = "{\"job_type\": \"SAMPLE_PROCESSOR\",\"reference\": \"123\",\"metadata\": {}}";

    @Test
    public void convertJobRequestJson() throws JsonProcessingException {
        JobRequest request = objectMapper.readValue(RETRY_TESTS, JobRequest.class);
        Assertions.assertNotNull(request);
        Assertions.assertEquals("123", request.reference());
    }

    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        SimpleModule module = new SimpleModule();
        module.addDeserializer(JobType.class, new SampleJobTypeDeserializer());
        objectMapper.registerModule(module);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper;
    }
}
