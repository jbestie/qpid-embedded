package org.jbestie.qpid;

import org.jbestie.qpid.config.QpidJMSProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@EnableConfigurationProperties({QpidJMSProperties.class})
public class QpidApplication {

    public static void main(String[] args) {
        SpringApplication.run(QpidApplication.class, args);
    }
}
