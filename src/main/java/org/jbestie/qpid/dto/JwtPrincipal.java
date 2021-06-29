package org.jbestie.qpid.dto;

import org.apache.qpid.server.model.AuthenticationProvider;
import org.apache.qpid.server.model.ConfiguredObject;
import org.apache.qpid.server.security.QpidPrincipal;

public class JwtPrincipal implements QpidPrincipal {

    private final String _accessToken;
    private final String _name;
    private final AuthenticationProvider<?> _authenticationProvider;

    public JwtPrincipal(String accessToken, String name, AuthenticationProvider<?> authenticationProvider) {
        _accessToken = accessToken;
        _name = name;
        _authenticationProvider = authenticationProvider;
    }

    public String getAccessToken()
    {
        return _accessToken;
    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public ConfiguredObject<?> getOrigin()
    {
        return _authenticationProvider;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final JwtPrincipal that = (JwtPrincipal) o;

        if (!_accessToken.equals(that._accessToken))
        {
            return false;
        }
        return _name.equals(that._name);

    }

    @Override
    public int hashCode()
    {
        int result = _accessToken.hashCode();
        result = 31 * result + _name.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
