package org.jbestie.qpid.sasl;

import org.apache.qpid.jms.sasl.Mechanism;
import org.apache.qpid.jms.sasl.MechanismFactory;

public class JwtMechanismFactory implements MechanismFactory {
    @Override
    public Mechanism createMechanism() {
        return new JwtMechanism();
    }
}
