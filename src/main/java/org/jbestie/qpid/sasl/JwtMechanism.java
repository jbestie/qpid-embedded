package org.jbestie.qpid.sasl;

import org.apache.qpid.jms.sasl.AbstractMechanism;

import javax.security.sasl.SaslException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

public class JwtMechanism extends AbstractMechanism {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String getName() {
        return "JWT";
    }

    @Override
    public byte[] getInitialResponse() throws SaslException {
        return getPassword().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] getChallengeResponse(byte[] challenge) throws SaslException {
        return EMPTY;
    }

    @Override
    public boolean isApplicable(String username, String password, Principal localPrincipal) {
        return true;
    }
}
