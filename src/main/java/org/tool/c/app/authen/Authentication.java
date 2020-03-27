package org.tool.c.app.authen;

import org.tool.c.services.http.HttpMethods;
import org.tool.c.services.http.ResponseEntity;
import org.tool.c.services.http.RestOperations;
import org.tool.c.utils.CommonUtils;
import org.tool.c.utils.CryptoUtils;
import org.tool.c.utils.constants.ActionConstants;
import org.tool.c.utils.constants.Constants;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Authentication class.
 */
public class Authentication {

    /**
     * Authentication by username and password.
     *
     * @param username username
     * @param password password
     * @return Authentication Token
     */
    public Token authenticate(String username, String password) throws IOException, NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ClassNotFoundException {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        String url = bundle.getString("tool.url.identity");
        String algorithm = bundle.getString("crypto.algorithm");

        RestOperations restOperation = new RestOperations();
        Map<String, Object> data = new HashMap<>();
        data.put("action", ActionConstants.GET_OAUTH_TOKEN);
        data.put("grantType", "password");
        data.put("grantData", Arrays.asList(username, CryptoUtils.decryptString(algorithm, password)));
        ResponseEntity<Token> responseEntity = restOperation.getForObject(url, HttpMethods.POST, Token.class, data);

        return CommonUtils.getObject(responseEntity);
    }

    /**
     * Get Access token.
     *
     * @param loginToken login token
     * @return return access token
     * @throws IOException
     */
    public Token getAccessToken(String loginToken) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        String url = bundle.getString("tool.url.identity");
        RestOperations restOperation = new RestOperations();
        Map<String, Object> data = new HashMap<>();
        data.put("action", ActionConstants.GET_ACCESS_TOKEN);
        data.put("loginToken", loginToken);
        ResponseEntity<Token> responseEntity = restOperation.getForObject(url, HttpMethods.POST, Token.class, data);

        return CommonUtils.getObject(responseEntity);
    }
}
