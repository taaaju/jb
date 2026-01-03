package oxygen.services.jobs_processor.config.queueing;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.activemq")
public class OxyJobActiveMQProperties {
  private String brokerUrl;
  private String user;
  private String password;
  private String defaultReplyTo;


  @PostConstruct
  private void postConstruct() {
      if (defaultReplyTo == null) {
          defaultReplyTo = "oxy-job-default-reply-queue";
      }
    log.info("{} {}", this.getClass().getName(), this);
  }


  @Override
  public String toString() {
    return "ActiveMQProperties{" +
        "brokerUrl='" + brokerUrl +
        ", defaultReplyTo='" + defaultReplyTo +
        '}';
  }
}
