package oxygen.services.jobs_processor.queueing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OxyJobMessengerService {
    private final JmsTemplate jmsTemplate;
    private final String orchestratorIncomingQueue;

    public OxyJobMessengerService(
            @Qualifier("oxyJobJmsTemplate") JmsTemplate jmsTemplate,
            @Value("${queues.orchestrator.incoming}") String orchestratorIncomingQueue
    ) {
        this.orchestratorIncomingQueue = orchestratorIncomingQueue;
        this.jmsTemplate = jmsTemplate;
    }

    public void deliverToOrchestrator(Object request) {
        deliverToQueue(request, this.orchestratorIncomingQueue);
    }

    private void deliverToQueue(Object request, String queueName) {
        try {
            jmsTemplate.convertAndSend(queueName, request);
        } catch (Exception e) {
            log.error("failed {} in queue ", queueName, e);
        }
    }
}