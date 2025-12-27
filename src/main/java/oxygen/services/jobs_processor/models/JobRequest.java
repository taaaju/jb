package oxygen.services.jobs_processor.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record JobRequest(
        @JsonProperty("job_type")
        JobType jobType,
        String reference,
        Map<String, Object> metadata
) {}
