package org.tool.c.app.action;

import org.tool.c.app.entity.Token;
import org.tool.c.bundle.AppBundle;
import org.tool.c.services.http.HttpMethods;
import org.tool.c.services.http.ResponseEntity;
import org.tool.c.services.http.RestOperations;
import org.tool.c.utils.CommonUtils;
import org.tool.c.utils.CryptoUtils;
import org.tool.c.utils.constants.Actions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication class. It use to get token authentication.
 *
 * @author hungp
 */
public class AuthenticationTokenAction extends AppBundle {

    public static final String KEY_ACTION = "action";
    public static final String KEY_GRANT_TYPE = "grantType";
    public static final String KEY_GRANT_DATA = "grantData";
    public static final String VALUE_GRANT_TYPE_PASSWORD = "password";

    /**
     * Authentication by username and password.
     *
     * @param username username
     * @param password password
     * @return Authentication Token
     */
    public Token authenticateIdentity(String username, String password) {
        RestOperations restOperation = new RestOperations();

        // Prepare data for request
        Map<String, Object> data = new HashMap<>();
        data.put(KEY_ACTION, Actions.GET_OAUTH_TOKEN);
        data.put(KEY_GRANT_TYPE, VALUE_GRANT_TYPE_PASSWORD);
        data.put(KEY_GRANT_DATA, Arrays.asList(username, CryptoUtils.decryptString(algorithm, password)));

        // Send request to get response
        ResponseEntity<Token> responseEntity = restOperation.getForObject(identityUrl, HttpMethods.POST, Token.class, data);

        return CommonUtils.getResponseObject(responseEntity);
    }
}
