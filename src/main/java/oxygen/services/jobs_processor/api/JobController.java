package oxygen.services.jobs_processor.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oxygen.services.jobs_processor.core.JobRouter;
import oxygen.services.jobs_processor.models.JobRequest;
import oxygen.services.jobs_processor.models.JobResult;

@RestController
@RequestMapping("/api/v1/oxy-jobs/processor")
public class JobController {

    private final JobRouter jobRouter;

    public JobController(JobRouter jobRouter) {
        this.jobRouter = jobRouter;
    }

    @PostMapping("/trigger")
    public ResponseEntity<JobResult> triggerJob(@RequestBody JobRequest request) {
        return ResponseEntity.ok().body(jobRouter.route(request));
    }
}
