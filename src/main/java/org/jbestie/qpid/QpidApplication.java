package org.jbestie.qpid;

import lombok.extern.slf4j.Slf4j;
import org.jbestie.qpid.config.QpidJMSProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

@Slf4j
@EnableJms
@EnableConfigurationProperties({QpidJMSProperties.class})
@SpringBootApplication
public class QpidApplication {

    @Bean
    EmbeddedBroker getEmbeddedBroker(QpidJMSProperties qpidJMSProperties) throws Exception {
        log.info("Bean created");
        EmbeddedBroker embeddedBroker = new EmbeddedBroker(qpidJMSProperties);
        embeddedBroker.start();

        return embeddedBroker;
    }

    public static void main(String[] args) {
        SpringApplication.run(QpidApplication.class, args);
    }
}
