package org.jbestie.qpid.provider;

import org.apache.commons.lang3.StringUtils;
import org.apache.qpid.server.model.AuthenticationProvider;
import org.apache.qpid.server.model.Container;
import org.apache.qpid.server.model.ManagedObject;
import org.apache.qpid.server.model.NamedAddressSpace;
import org.apache.qpid.server.security.auth.AuthenticationResult;
import org.apache.qpid.server.security.auth.manager.AbstractAuthenticationManager;
import org.apache.qpid.server.security.auth.sasl.SaslNegotiator;
import org.apache.qpid.server.security.auth.sasl.SaslSettings;
import org.jbestie.qpid.dto.JwtPrincipal;
import org.jbestie.qpid.sasl.JwtSaslNegotiator;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ManagedObject(category = false, type = "JWT")
public class JwtAuthenticationProvider extends AbstractAuthenticationManager<JwtAuthenticationProvider> implements AuthenticationProvider<JwtAuthenticationProvider> {

    public JwtAuthenticationProvider(Map attributes, Container container) {
        super(attributes, container);
    }

    @Override
    public List<String> getMechanisms() {
        return Collections.singletonList("JWT");
    }

    @Override
    public SaslNegotiator createSaslNegotiator(String mechanism, SaslSettings saslSettings, NamedAddressSpace addressSpace) {
        return new JwtSaslNegotiator(this);
    }

    public AuthenticationResult authenticateViaAccessToken(String auth) {
        if (validJwtToken(auth)) {
            return new AuthenticationResult(new JwtPrincipal(auth, auth, this));
        }

        return new AuthenticationResult(AuthenticationResult.AuthenticationStatus.ERROR);
    }

    private boolean validJwtToken(String auth) {
        JsonWebSignature jsonWebSignature = new JsonWebSignature();
        String jsonToken = StringUtils.defaultString(auth);
        try {
            jsonWebSignature.setCompactSerialization(jsonToken.substring("Bearer ".length()));
            JwtClaims jwtClaims = JwtClaims.parse(jsonWebSignature.getUnverifiedPayload());

            Key key = new HmacKey("123412341234123412341234123412341234".getBytes(StandardCharsets.UTF_8));
            jsonWebSignature.setKey(key);
            return "guest".equals(jwtClaims.getClaimValue("user", String.class))
                    && jsonWebSignature.verifySignature();
        } catch (JoseException | InvalidJwtException | MalformedClaimException e) {
            return false;
        }
    }
}
