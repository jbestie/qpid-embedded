package org.jbestie.qpid;

import org.apache.qpid.server.SystemLauncher;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class EmbeddedBroker {
    private static final String INITIAL_CONFIGURATION = "test-initial-config.json";

    private final SystemLauncher systemLauncher;

    public EmbeddedBroker() throws Exception {
        systemLauncher = new SystemLauncher();
        systemLauncher.startup(createSystemConfig());
    }

    @PreDestroy
    public void stop() {
        systemLauncher.shutdown();
    }

    private Map<String, Object> createSystemConfig() {
        System.setProperty("qpid.type.disabled:plugin.MANAGEMENT-HTTP", String.valueOf(Boolean.TRUE));
        Map<String, Object> attributes = new HashMap<>();
        URL initialConfig = EmbeddedBroker.class.getClassLoader().getResource(INITIAL_CONFIGURATION);
        attributes.put("type", "Memory");
        attributes.put("initialConfigurationLocation", Objects.requireNonNull(initialConfig).toExternalForm());
        attributes.put("startupLoggedToSystemOut", true);
        return attributes;
    }
}