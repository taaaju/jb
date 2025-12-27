package oxygen.services.jobs_processor.models;

import java.util.Map;

public record JobContext(
        String reference,
        Map<String, Object> metadata
) {}
