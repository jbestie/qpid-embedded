package org.jbestie.qpid;

import lombok.extern.slf4j.Slf4j;
import org.apache.qpid.server.SystemLauncher;
import org.apache.qpid.server.SystemLauncherListener;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EmbeddedBroker {

    private final SystemLauncher qpidLauncher;

    public EmbeddedBroker() {
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
        final Map<String, Object> attributes = new HashMap<>();
        String configFilePath = "qpid-config.json";
        final URL initialConfig = EmbeddedBroker.class.getClassLoader().getResource(configFilePath);
        attributes.put("type", "Memory");
        attributes.put("initialConfigurationLocation", initialConfig != null ? initialConfig.toExternalForm() : null);
        attributes.put("startupLoggedToSystemOut", true);
        int port = 5672;
        System.setProperty("qpid.amqp_port", String.valueOf(port));
        return attributes;
    }

}