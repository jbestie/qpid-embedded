package org.jbestie.qpid;

import org.apache.qpid.server.SystemLauncher;
import org.apache.qpid.server.SystemLauncherListener;
import org.apache.qpid.server.model.*;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public class EmbeddedBroker {
    private static final String INITIAL_CONFIGURATION = "test-initial-config.json";

    private final SystemLauncher systemLauncher;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public EmbeddedBroker() throws Exception {
        systemLauncher = new SystemLauncher(new SystemLauncherListener.DefaultSystemLauncherListener() {
            private SystemConfig systemConfig;

            @Override
            public void onContainerResolve(SystemConfig<?> systemConfig) {
                this.systemConfig = systemConfig;
                super.onContainerResolve(systemConfig);
            }

            @Override
            public void beforeStartup() {
                super.beforeStartup();
            }

            @Override
            public void afterStartup() {
                super.afterStartup();
                Broker broker = (Broker) systemConfig.getContainer();

                createAnonymousAuthenticationProvider(broker);
                createAmqpPort(broker);
                VirtualHostNode virtualHostNodeCustom = createVirtualHostNode(broker);
                VirtualHost virtualHost = createVirtualHost(virtualHostNodeCustom);
                createQueues(virtualHost);
            }

            private void createAmqpPort(Broker broker) {
                Map<String, Object> portProperties = new HashMap<>();
                portProperties.put("name", "AMQP");
                portProperties.put("port", 5672);
                portProperties.put("authenticationProvider", "anonymous");
                broker.createChild(Port.class, portProperties);
            }

            private void createAnonymousAuthenticationProvider(Broker broker) {
                Map<String, Object> authenticationProviderProperties = new HashMap<>();
                authenticationProviderProperties.put("name", "anonymous");
                authenticationProviderProperties.put("type", "Anonymous");
                broker.createChild(AuthenticationProvider.class, authenticationProviderProperties);
            }

            private void createQueues(VirtualHost virtualHost) {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("durable", true);
                parameters.put("name", "testqueue");
                parameters.put("id", UUID.fromString("aeffc4be-440e-49d2-82f8-b05e8f224455"));
                parameters.put("type", "standard");
                parameters.put("holdOnPublishEnabled", false);
                virtualHost.createChild(Queue.class, parameters);
            }

            private VirtualHost createVirtualHost(VirtualHostNode virtualHostNodeCustom) {
                Map<String, Object> virtualHostParameters = new HashMap<>();
                virtualHostParameters.put( "id", UUID.fromString("ea334eaf-6734-4726-bf6d-c031cc55f11c"));
                virtualHostParameters.put("name", "localhost");
                virtualHostParameters.put("type", "JDBC");
                virtualHostParameters.put("desiredState", "ACTIVE");
                virtualHostParameters.put("connectionPoolType", "BONECP");
                virtualHostParameters.put("connectionUrl", "jdbc:h2:file:./work/db;CIPHER=AES");
                virtualHostParameters.put("password", "filepwd userpwd");
                virtualHostParameters.put("username", "sa");
                return (VirtualHost) virtualHostNodeCustom.createChild(VirtualHost.class, virtualHostParameters);
            }

            @SuppressWarnings({"rawtypes", "unchecked"})
            private VirtualHostNode createVirtualHostNode(Broker broker) {
                Map<String, Object> virtualHostNodeParameters = new HashMap<>();
                virtualHostNodeParameters.put("id", UUID.fromString("ea334eaf-6734-4726-bf6d-c031cc55f11c"));
                virtualHostNodeParameters.put("name", "localhost");
                virtualHostNodeParameters.put("type", "Memory");
                virtualHostNodeParameters.put("defaultVirtualHostNode", "true");
                return (VirtualHostNode) broker.createChild(VirtualHostNode.class, virtualHostNodeParameters);
            }
        });
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