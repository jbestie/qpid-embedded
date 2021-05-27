package org.jbestie.qpid;

import lombok.RequiredArgsConstructor;
import org.jbestie.qpid.config.Const;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = Const.JMS_QUEUE)
    public void processMsg(Message message) throws JMSException {
        System.out.println("============= Received: " + message.getBody(String.class));

        Destination destination = message.getJMSReplyTo();
        jmsTemplate.convertAndSend(destination, message.getBody(String.class).toUpperCase());
    }
}