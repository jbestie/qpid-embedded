package org.jbestie.qpid;

import lombok.extern.slf4j.Slf4j;
import org.apache.qpid.server.SystemLauncher;
import org.apache.qpid.server.SystemLauncherListener;
import org.jbestie.qpid.config.QpidJMSProperties;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EmbeddedBroker {
    private static final String INITIAL_CONFIGURATION = "test-initial-config.json";

    private final QpidJMSProperties qpidJMSProperties;

    private final SystemLauncher qpidLauncher;

    public EmbeddedBroker(QpidJMSProperties qpidJMSProperties) {
        this.qpidJMSProperties = qpidJMSProperties;
        this.qpidLauncher = new SystemLauncher(new SystemLauncherListener.DefaultSystemLauncherListener());
    }

    public void start() throws Exception {
        log.info("Starting Embedded Qpid broker");
        this.qpidLauncher.startup(createSystemConfig());
        log.info("Started Embedded Qpid broker");
    }

    public void stop() {
        log.info("Stopping Embedded Qpid broker");
        this.qpidLauncher.shutdown();
        log.info("Stopped Embedded Qpid broker");
    }


    private Map<String, Object> createSystemConfig() {
        URL initialConfig = EmbeddedBroker.class.getClassLoader().getResource(INITIAL_CONFIGURATION);
        final Map<String, Object> attributes = new HashMap<>();
        attributes.put("type", "Memory");
        attributes.put("initialConfigurationLocation", initialConfig != null ? initialConfig.toExternalForm() : null);
        attributes.put("startupLoggedToSystemOut", true);
        int port = 5672;
        System.setProperty("qpid.amqp_port", String.valueOf(port));
        System.setProperty("qpid.plain.username", qpidJMSProperties.getUsername());
        System.setProperty("qpid.plain.password", qpidJMSProperties.getPassword());
        System.setProperty("qpid.jdbc.username", qpidJMSProperties.getJdbcUsername());
        System.setProperty("qpid.jdbc.password", qpidJMSProperties.getJdbcPassword());
        System.setProperty("qpid.keystore_pass", qpidJMSProperties.getKeystorePassword());
        System.setProperty("qpid.keystore_path", qpidJMSProperties.getKeystorePath());
        return attributes;
    }

}