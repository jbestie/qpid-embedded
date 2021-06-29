package org.jbestie.qpid.factory;

import org.apache.qpid.server.model.AbstractConfiguredObjectTypeFactory;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.plugin.PluggableService;
import org.jbestie.qpid.provider.JwtAuthenticationProvider;

import java.util.Map;

@PluggableService
public class JwtConfiguredObjectTypeFactory extends AbstractConfiguredObjectTypeFactory<JwtAuthenticationProvider> {
    public JwtConfiguredObjectTypeFactory() {
        super(JwtAuthenticationProvider.class);
    }

    @Override
    protected JwtAuthenticationProvider createInstance(Map<String, Object> attributes, ConfiguredObject<?> parent) {
        return new JwtAuthenticationProvider(attributes, (org.apache.qpid.server.model.Broker)parent);
    }
}
