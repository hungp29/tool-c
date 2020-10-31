package org.tool.c.app.action;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.tool.c.app.entity.Condition;
import org.tool.c.app.entity.Scope;
import org.tool.c.app.entity.Token;
import org.tool.c.app.entity.User;
import org.tool.c.bundle.AppBundle;
import org.tool.c.services.http.HttpMethods;
import org.tool.c.services.http.ResponseEntity;
import org.tool.c.services.http.RestOperations;
import org.tool.c.utils.CommonUtils;
import org.tool.c.utils.CryptoUtils;
import org.tool.c.utils.constants.Actions;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication class.
 */
public class Authentication extends AppBundle {

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
        data.put("action", Actions.GET_OAUTH_TOKEN);
        data.put("grantType", "password");
        data.put("grantData", Arrays.asList(username, CryptoUtils.decryptString(algorithm, password)));

        // Send request to get response
        ResponseEntity<Token> responseEntity = restOperation.getForObject(identityUrl, HttpMethods.POST, Token.class, data);

        return CommonUtils.getResponseObject(responseEntity);
    }

    /**
     * Get Access token.
     *
     * @param loginToken login token
     * @return return access token
     */
    public Token getAccessToken(String appUrl, String loginToken) {
        String fieldNameAction = appUrl.equals(identityUrl) ? "action" : "action_name";
        RestOperations restOperation = new RestOperations();

        // Prepare data for request
        Map<String, Object> data = new HashMap<>();
        data.put(fieldNameAction, Actions.GET_ACCESS_TOKEN);
        data.put("loginToken", loginToken);

        // Send request to get response
        ResponseEntity<Token> responseEntity = restOperation.getForObject(appUrl, HttpMethods.POST, Token.class, data);

        return CommonUtils.getResponseObject(responseEntity);
    }

    /**
     * Get Oauth app.
     *
     * @param accessToken the access token
     * @return value
     */
    public Map<String, String> getOauthApp(String accessToken) {
        RestOperations restOperation = new RestOperations();

        // Prepare data for request
        Map<String, Object> data = new HashMap<>();
        data.put("action", Actions.GET_OAUTH_APP);
        data.put("id", 1);
        data.put("info", Arrays.asList("id", "name", "callbackUrls"));

        // Send request to get response
        ResponseEntity<Map<String, String>> responseEntity = restOperation.getForMap(identityUrl, HttpMethods.POST, accessToken, String.class, data);

        return CommonUtils.getResponseObject(responseEntity);
    }

    /**
     * Get code for oauth request.
     *
     * @param oauthAppMap Oauth app map
     * @param accessToken access token
     * @return code
     */
    public String getCodeOauthRequest(Map<String, String> oauthAppMap, String accessToken) {
        RestOperations restOperation = new RestOperations();

        // Get user id from access token
        String payloadToken = accessToken.substring(0, accessToken.lastIndexOf(".") + 1);
        Jwt<Header, Claims> untrusted = Jwts.parserBuilder().build().parseClaimsJwt(payloadToken);
        int userId = untrusted.getBody().get("userId", Integer.class);

        // Prepare data
        Condition condition = new Condition();
        condition.setSubset(Arrays.asList("id", "username", "profilePhoto", "branchId", "branch"));
        Scope scope = new Scope();
        scope.setName("user.read");
        scope.setCondition(condition);

        Map<String, Object> data = new HashMap<>();
        data.put("action", Actions.CREATE_OAUTH_REQUEST);
        data.put("appId", oauthAppMap.get("id"));
        data.put("userId", userId);
        data.put("scope", Collections.singletonList(scope));
        data.put("callbackUrl", CommonUtils.getValueFromArrayString((String) oauthAppMap.get("callbackUrls")));

        ResponseEntity<Map<String, String>> responseEntity = restOperation.getForMap(identityUrl, HttpMethods.POST, accessToken, String.class, data);
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
     */
    public User loginCheckinApp(Map<String, ?> oauthAppMap, String codeOauth) {
        RestOperations restOperation = new RestOperations();

        Map<String, Object> data = new HashMap<>();
        data.put("action_name", Actions.LOGIN);
        data.put("grantType", "oauth");
        data.put("grantData", Arrays.asList("identity", codeOauth, CommonUtils.getValueFromArrayString((String) oauthAppMap.get("callbackUrls"))));

        ResponseEntity<User> responseEntity = restOperation.getForObject(checkinUrl, HttpMethods.POST, User.class, data);
        return CommonUtils.getResponseObject(responseEntity);
    }
}
