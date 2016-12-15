package org.filestore.api.oauth2;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by nitix on 14/12/16.
 */
public abstract class Generic {

    protected Configuration configuration;

    public Generic(Configuration configuration) {
        this.configuration = configuration;
    }

    public static final String scope = "email";

    public abstract OAuthProviderType getProvider();

    public OAuthClientRequest createCodeRequest() throws OAuthSystemException {
        return OAuthClientRequest
                .authorizationProvider(getProvider())
                .setClientId(getClientId())
                .setRedirectURI(getRedirectUri())
                .setScope(getScope())
                .setResponseType(ResponseType.CODE.toString())
                .buildQueryMessage();
    }

    public OAuthClientRequest createTokenRequest(String code) throws OAuthSystemException {
        return OAuthClientRequest.tokenProvider(OAuthProviderType.GITHUB)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(getClientId())
                .setClientSecret(getClientSecret())
                .setRedirectURI(getRedirectUri())
                .setCode(code)
                .buildQueryMessage();
    }


    private String getRedirectUri() {
        return configuration.getProviderConfiguration(getProvider()).getRedirectUri();
    }

    private String getClientId() {
        return configuration.getProviderConfiguration(getProvider()).getClientId();
    }

    protected String getClientSecret() {
        return configuration.getProviderConfiguration(getProvider()).getClientSecret();
    }

    protected abstract String getScope();

    public abstract String getUserEmail(String token) throws IOException;
}
