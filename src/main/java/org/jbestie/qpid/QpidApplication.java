package org.jbestie.qpid;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.jbestie.qpid.config.QpidJMSProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@EnableConfigurationProperties({QpidJMSProperties.class})
public class QpidApplication {

    @Bean
    JmsConnectionFactory jmsConnectionFactory(QpidJMSProperties qpidJMSProperties) {
        JmsConnectionFactory jmsConnectionFactory = new JmsConnectionFactory();
        jmsConnectionFactory.setUsername(qpidJMSProperties.getUsername());
        jmsConnectionFactory.setPassword(qpidJMSProperties.getPassword());
        jmsConnectionFactory.setRemoteURI(qpidJMSProperties.getRemoteURL());
        return jmsConnectionFactory;
    }

    public static void main(String[] args) {
        SpringApplication.run(QpidApplication.class, args);
    }
}
