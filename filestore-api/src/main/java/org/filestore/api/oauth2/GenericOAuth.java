package org.filestore.api.oauth2;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.io.IOException;

/**
 * Created by nitix on 16/12/16.
 */
@Remote
public interface GenericOAuth  {
    OAuthClientRequest createCodeRequest() throws OAuthSystemException;

    OAuthClientRequest createTokenRequest(String code) throws OAuthSystemException;

    String getUserEmail(String token) throws IOException;

    OAuthClientRequest createTokenRequest(String user, String password) throws OAuthSystemException;
}
