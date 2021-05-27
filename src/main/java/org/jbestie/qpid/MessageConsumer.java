package org.jbestie.qpid;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

@Component
@RequiredArgsConstructor
public class MessageConsumer {
    public static final String JMS_QUEUE = "testqueue";

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JMS_QUEUE)
    public void processMsg(Message message) throws JMSException {
        System.out.println("============= Received: " + message.getBody(String.class));

        Destination destination = message.getJMSReplyTo();
        jmsTemplate.convertAndSend(destination, message.getBody(String.class).toUpperCase());
    }
}