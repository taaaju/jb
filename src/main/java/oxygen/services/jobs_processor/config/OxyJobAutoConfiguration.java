package oxygen.services.jobs_processor.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import oxygen.services.jobs_processor.core.OxyJobExecutor;
import oxygen.services.jobs_processor.core.OxyJobHandler;
import oxygen.services.jobs_processor.core.OxyJobRouter;

import java.util.List;

@AutoConfiguration
@Configuration
@ConditionalOnClass(OxyJobHandler.class)
@ComponentScan(basePackages = "oxygen.services.jobs_processor")
public class OxyJobAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OxyJobRouter jobRouter(List<OxyJobHandler> handlers, OxyJobExecutor executor) {
        return new OxyJobRouter(handlers, executor);
    }
}
