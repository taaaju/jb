package oxygen.services.jobs_processor.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import oxygen.services.jobs_processor.core.JobExecutor;
import oxygen.services.jobs_processor.core.JobHandler;
import oxygen.services.jobs_processor.core.JobRouter;

import java.util.List;

@AutoConfiguration
@Configuration
@ConditionalOnClass(JobHandler.class)
@ComponentScan(basePackages = "oxygen.services.jobs_processor")
public class JobAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JobRouter jobRouter(List<JobHandler> handlers, JobExecutor executor) {
        return new JobRouter(handlers, executor);
    }
}
