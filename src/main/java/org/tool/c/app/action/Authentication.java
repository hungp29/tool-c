package org.tool.c.app.action;

import org.tool.c.app.entity.Condition;
import org.tool.c.app.entity.Scope;
import org.tool.c.app.entity.Token;
import org.tool.c.app.entity.User;
import org.tool.c.base.Base;
import org.tool.c.exception.CryptoException;
import org.tool.c.services.http.HttpMethods;
import org.tool.c.services.http.ResponseEntity;
import org.tool.c.services.http.RestOperations;
import org.tool.c.utils.CommonUtils;
import org.tool.c.utils.CryptoUtils;
import org.tool.c.utils.constants.Actions;
import org.tool.c.utils.constants.Constants;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Authentication class.
 */
public class Authentication extends Base {

    /**
     * Authentication by username and password.
     *
     * @param username username
     * @param password password
     * @return Authentication Token
     */
    public Token authenticate(String username, String password) throws IOException, CryptoException {

        RestOperations restOperation = new RestOperations();
        Map<String, Object> data = new HashMap<>();
        data.put("action", Actions.GET_OAUTH_TOKEN);
        data.put("grantType", "password");
        data.put("grantData", Arrays.asList(username, CryptoUtils.decryptString(algorithm, password)));
        ResponseEntity<Token> responseEntity = restOperation.getForObject(identityUrl, HttpMethods.POST, Token.class, data);

        return CommonUtils.getResponseObject(responseEntity);
    }

    /**
     * Get Access token.
     *
     * @param loginToken login token
     * @return return access token
     * @throws IOException
     */
    public Token getAccessToken(String appUrl, String loginToken) throws IOException {
        String fieldNameAction = appUrl.equals(identityUrl) ? "action" : "action_name";
        RestOperations restOperation = new RestOperations();
        Map<String, Object> data = new HashMap<>();
        data.put(fieldNameAction, Actions.GET_ACCESS_TOKEN);
        data.put("loginToken", loginToken);
        ResponseEntity<Token> responseEntity = restOperation.getForObject(appUrl, HttpMethods.POST, Token.class, data);

        return CommonUtils.getResponseObject(responseEntity);
    }

    /**
     * Get Oauth app.
     *
     * @param accessToken the access token
     * @return value
     */
    public Map<String, ?> getOauthApp(String accessToken) throws IOException {
        RestOperations restOperation = new RestOperations();
        Map<String, Object> data = new HashMap<>();
        data.put("action", Actions.GET_OAUTH_APP);
        data.put("id", 1);
        data.put("info", Arrays.asList("id", "name", "callbackUrls"));

        ResponseEntity<Map> responseEntity = restOperation.getForObject(identityUrl, HttpMethods.POST, accessToken, Map.class, data);
        return CommonUtils.getResponseObject(responseEntity);
    }

    /**
     * Get code for oauth request.
     *
     * @param oauthAppMap Oauth app map
     * @param accessToken access token
     * @return code
     * @throws IOException
     */
    public String getCodeOauthRequest(Map<String, ?> oauthAppMap, String accessToken) throws IOException {
        RestOperations restOperation = new RestOperations();

        // Prepare data
        Condition condition = new Condition();
        condition.setSubset(Arrays.asList("id", "username", "profilePhoto", "branchId", "branch"));
        Scope scope = new Scope();
        scope.setName("user.read");
        scope.setCondition(condition);

        Map<String, Object> data = new HashMap<>();
        data.put("action", Actions.CREATE_OAUTH_REQUEST);
        data.put("appId", oauthAppMap.get("id"));
        data.put("userId", Integer.parseInt(bundle.getString("tool.user.id")));
        data.put("scope", Arrays.asList(scope));
        data.put("callbackUrl", CommonUtils.getValueFromArrayString((String) oauthAppMap.get("callbackUrls")));

        ResponseEntity<Map> responseEntity = restOperation.getForObject(identityUrl, HttpMethods.POST, accessToken, Map.class, data);
        Map<String, String> result = CommonUtils.getResponseObject(responseEntity);

        if (!CommonUtils.isEmpty(result)) {
            return result.get("code");
        } else {
            return null;
        }
    }

    /**
     * Login to checkin app.
     *
     * @param oauthAppMap Oauth app data
     * @param codeOauth   code for oauth
     * @return user information, it include token data
     * @throws IOException
     */
    public User loginCheckinApp(Map<String, ?> oauthAppMap, String codeOauth) throws IOException {
        RestOperations restOperation = new RestOperations();

        Map<String, Object> data = new HashMap<>();
        data.put("action_name", Actions.LOGIN);
        data.put("grantType", "oauth");
        data.put("grantData", Arrays.asList("identity", codeOauth, CommonUtils.getValueFromArrayString((String) oauthAppMap.get("callbackUrls"))));

        ResponseEntity<User> responseEntity = restOperation.getForObject(checkinUrl, HttpMethods.POST, User.class, data);
        return CommonUtils.getResponseObject(responseEntity);
    }
}
