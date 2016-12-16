package org.filestore.ejb.config;

import org.apache.oltu.oauth2.common.OAuthProviderType;

/**
 * Created by nitix on 14/12/16.
 */
public class ProviderConfiguration {

    private final OAuthProviderType provider;

    private final String clientId;

    private final String clientSecret;

    private final String redirectUri;

    public ProviderConfiguration(OAuthProviderType provider, String clientId, String clientSecret, String redirectUri) {
        this.provider = provider;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public OAuthProviderType getProvider() {
        return provider;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
