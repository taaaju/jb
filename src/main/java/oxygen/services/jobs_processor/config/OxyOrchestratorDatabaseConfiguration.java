package oxygen.services.jobs_processor.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "oxygen.services.jobs_processor.persistence.repositories",
        entityManagerFactoryRef = "orchestratorEntityManagerFactory",
        transactionManagerRef = "orchestratorTransactionManager"
)
public class OxyOrchestratorDatabaseConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.orchestrator")
    public DataSource orchestratorDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean orchestratorEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("orchestratorDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("oxygen.services.jobs_processor.persistence.entities")
                .persistenceUnit("orchestrator")
                .build();
    }

    @Bean
    public PlatformTransactionManager orchestratorTransactionManager(
            @Qualifier("orchestratorEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}