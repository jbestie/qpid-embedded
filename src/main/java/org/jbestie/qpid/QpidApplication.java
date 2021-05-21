package org.jbestie.qpid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class QpidApplication {

    @Bean
    EmbeddedBroker getEmbeddedBroker() throws Exception {
        log.info("Bean created");
        EmbeddedBroker embeddedBroker = new EmbeddedBroker();
        embeddedBroker.start();

        return embeddedBroker;
    }

    public static void main(String[] args) {
        SpringApplication.run(QpidApplication.class, args);
    }
}
