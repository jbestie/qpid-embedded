package org.jbestie.qpid.sasl;

import lombok.extern.slf4j.Slf4j;
import org.apache.qpid.server.security.auth.AuthenticationResult;
import org.apache.qpid.server.security.auth.sasl.SaslNegotiator;
import org.jbestie.qpid.provider.JwtAuthenticationProvider;

import java.nio.charset.StandardCharsets;

@Slf4j
public class JwtSaslNegotiator implements SaslNegotiator {
    enum State
    {
        INITIAL,
        CHALLENGE_SENT,
        COMPLETE
    }

    private static final String BEARER_PREFIX = "Bearer ";
    private volatile JwtSaslNegotiator.State _state = JwtSaslNegotiator.State.INITIAL;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public JwtSaslNegotiator(JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    @Override
    public AuthenticationResult handleResponse(byte[] response) {
        if (_state == JwtSaslNegotiator.State.COMPLETE)
        {
            return new AuthenticationResult(AuthenticationResult.AuthenticationStatus.ERROR,
                    new IllegalStateException("Multiple Authentications not permitted."));
        }
        else if (_state == JwtSaslNegotiator.State.INITIAL && (response == null || response.length == 0))
        {
            _state = JwtSaslNegotiator.State.CHALLENGE_SENT;
            return new AuthenticationResult(new byte[0], AuthenticationResult.AuthenticationStatus.CONTINUE);
        }

        _state = JwtSaslNegotiator.State.COMPLETE;
        if (response == null || response.length == 0)
        {
            return new AuthenticationResult(AuthenticationResult.AuthenticationStatus.ERROR,
                    new IllegalArgumentException("Invalid OAuth2 client response."));
        }

        String auth = new String(response, StandardCharsets.US_ASCII);
        if (auth.startsWith(BEARER_PREFIX))
        {
            return jwtAuthenticationProvider.authenticateViaAccessToken(auth);
        }
        else
        {
            return new AuthenticationResult(AuthenticationResult.AuthenticationStatus.ERROR, new IllegalArgumentException("Wrong Bearer token"));
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public String getAttemptedAuthenticationId() {
        return null;
    }
}
