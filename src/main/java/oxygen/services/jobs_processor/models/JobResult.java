package oxygen.services.jobs_processor.models;

public record JobResult(
        JobState jobState,
        boolean success,
        String message
) {}

