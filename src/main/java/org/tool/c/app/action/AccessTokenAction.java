package org.tool.c.app.action;

import org.tool.c.app.entity.Token;
import org.tool.c.bundle.AppBundle;
import org.tool.c.services.http.HttpMethods;
import org.tool.c.services.http.ResponseEntity;
import org.tool.c.services.http.RestOperations;
import org.tool.c.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Access Token action. It use to get access token.
 *
 * @author hungp
 */
public class AccessTokenAction extends AppBundle {

    public static final String KEY_ACTION_IDENTITY = "action";
    public static final String KEY_ACTION_CHECKIN = "action_name";
    public static final String KEY_LOGIN_TOKEN = "loginToken";
    public static final String VALUE_ACTION = "get_access_token";

    /**
     * Get Access token.
     *
     * @param loginToken login token
     * @return return access token
     */
    public Token getAccessToken(String appUrl, String loginToken) {
        RestOperations restOperation = new RestOperations();
        final String KEY_ACTION = appUrl.equals(identityUrl) ? KEY_ACTION_IDENTITY : KEY_ACTION_CHECKIN;

        // Prepare data for request
        Map<String, Object> data = new HashMap<>();
        data.put(KEY_ACTION, VALUE_ACTION);
        data.put(KEY_LOGIN_TOKEN, loginToken);

        // Send request to get response
        ResponseEntity<Token> responseEntity = restOperation.getForObject(appUrl, HttpMethods.POST, Token.class, data);

        return CommonUtils.getResponseObject(responseEntity);
    }
}
