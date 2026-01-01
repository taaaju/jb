package oxygen.services.jobs_processor.config.queueing;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.QosSettings;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

@Slf4j
@Configuration
@EnableJms
public class OxyJobActiveMQConfiguration {
  private final OxyJobActiveMQProperties oxyJobActiveMQProperties;
  private final ObjectMapper objectMapper;

  public OxyJobActiveMQConfiguration(OxyJobActiveMQProperties oxyJobActiveMQProperties, ObjectMapper objectMapper) {
    this.oxyJobActiveMQProperties = oxyJobActiveMQProperties;
      this.objectMapper = objectMapper;
  }

  @Bean("oxyJobActiveMQConnectionFactory")
  public ActiveMQConnectionFactory oxyJobActiveMQConnectionFactory() {
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
    connectionFactory.setTrustAllPackages(true);
    connectionFactory.setBrokerURL(this.oxyJobActiveMQProperties.getBrokerUrl());
    connectionFactory.setPassword(this.oxyJobActiveMQProperties.getPassword());
    connectionFactory.setUserName(this.oxyJobActiveMQProperties.getUser());
    return connectionFactory;
  }

  @Bean("oxyJobMessageConverter")
  public MessageConverter oxyJobMessageConverter() {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setObjectMapper(objectMapper);
    return converter;
  }

  @Bean("oxyJobJmsTemplate")
  public JmsTemplate jmsTemplate() {
    JmsTemplate template = new JmsTemplate();
    template.setConnectionFactory(oxyJobActiveMQConnectionFactory());
    template.setMessageConverter(oxyJobMessageConverter());
    template.setPubSubDomain(true);
    template.setDestinationResolver(oxyJobDestinationResolver());
    template.setDeliveryPersistent(true);

    return template;
  }

  @Bean("oxyJobDestinationResolver")
  DynamicDestinationResolver oxyJobDestinationResolver() {
    return new DynamicDestinationResolver() {
      @Override
      public Destination resolveDestinationName(
        Session session,
        String destinationName,
        boolean pubSubDomain
      ) throws JMSException {
        pubSubDomain = destinationName.endsWith("Topic");
        return super.resolveDestinationName(session, destinationName, pubSubDomain);
      }
    };
  }

  @Bean("oxyJobJmsListenerContainerFactory")
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(OxyExampleErrorHandler errorHandler) {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setConnectionFactory(oxyJobActiveMQConnectionFactory());
    factory.setErrorHandler(errorHandler);
    QosSettings replyQosSettings = new QosSettings();
    replyQosSettings.setPriority(2);
    replyQosSettings.setTimeToLive(10000);
    factory.setReplyQosSettings(replyQosSettings);
    return factory;

  }

  @Service
  public static class OxyExampleErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
      log.error("Error in listener", t);

    }
  }

  @Bean("oxyJobReplyQueue")
  public Queue replyQueue() {
    return new ActiveMQQueue(this.oxyJobActiveMQProperties.getDefaultReplyTo());
  }
}
