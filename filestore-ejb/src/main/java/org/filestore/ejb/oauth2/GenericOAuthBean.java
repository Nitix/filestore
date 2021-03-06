package org.filestore.ejb.oauth2;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.filestore.api.oauth2.GenericOAuth;
import org.filestore.ejb.config.OAuthConfig;

import javax.inject.Inject;

/**
 * Created by nitix on 14/12/16.
 */
public abstract class GenericOAuthBean implements GenericOAuth {

    @Inject
    protected  OAuthConfig configuration;

    public static final String scope = "email";


    public abstract OAuthProviderType getProvider();

    @Override
    public OAuthClientRequest createCodeRequest() throws OAuthSystemException {
        return OAuthClientRequest
                .authorizationProvider(getProvider())
                .setClientId(getClientId())
                .setRedirectURI(getRedirectUri())
                .setScope(getScope())
                .setResponseType(ResponseType.CODE.toString())
                .buildQueryMessage();
    }

    @Override
    public OAuthClientRequest createTokenRequest(String code) throws OAuthSystemException {
        return OAuthClientRequest.tokenProvider(getProvider())
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(getClientId())
                .setClientSecret(getClientSecret())
                .setRedirectURI(getRedirectUri())
                .setCode(code)
                .buildQueryMessage();
    }

    @Override
    public OAuthClientRequest createTokenRequest(String user, String password) throws OAuthSystemException {
        return OAuthClientRequest.tokenProvider(getProvider())
                .setGrantType(GrantType.PASSWORD)
                .setClientId(getClientId())
                .setClientSecret(getClientSecret())
                .setUsername(user)
                .setPassword(password)
                .buildQueryMessage();
    }

    protected String getRedirectUri() {
        return configuration.getProviderConfiguration(getProvider()).getRedirectUri();
    }

    protected String getClientId() {
        return configuration.getProviderConfiguration(getProvider()).getClientId();
    }

    protected String getClientSecret() {
        return configuration.getProviderConfiguration(getProvider()).getClientSecret();
    }

    protected abstract String getScope();

}
