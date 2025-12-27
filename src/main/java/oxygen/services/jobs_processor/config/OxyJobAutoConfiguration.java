package oxygen.services.jobs_processor.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import oxygen.services.jobs_processor.core.OxyJobExecutor;
import oxygen.services.jobs_processor.core.OxyJobHandler;
import oxygen.services.jobs_processor.core.OxyJobRouter;

import java.util.List;

@AutoConfiguration
@Configuration
@ConditionalOnClass(OxyJobHandler.class)
@ComponentScan(basePackages = {
        "oxygen.services.jobs_processor.core",
        "oxygen.services.jobs_processor.api",
        "oxygen.services.jobs_processor.config"
})
@EntityScan(basePackages = "oxygen.services.jobs_processor.persistence")
@EnableJpaRepositories(basePackages = "oxygen.services.jobs_processor.persistence")
public class OxyJobAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OxyJobRouter jobRouter(List<OxyJobHandler> handlers, OxyJobExecutor executor) {
        return new OxyJobRouter(handlers, executor);
    }
}
