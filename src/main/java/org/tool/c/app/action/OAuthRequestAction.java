package org.tool.c.app.action;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.tool.c.app.entity.Condition;
import org.tool.c.app.entity.Scope;
import org.tool.c.bundle.AppBundle;
import org.tool.c.services.http.HttpMethods;
import org.tool.c.services.http.ResponseEntity;
import org.tool.c.services.http.RestOperations;
import org.tool.c.utils.CommonUtils;
import org.tool.c.utils.constants.Actions;
import org.tool.c.utils.constants.Constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * OAuth request action. It use to create OAuth Request.
 *
 * @author hungp
 */
public class OAuthRequestAction extends AppBundle {

    public static final String KEY_ACTION = "action";
    public static final String KEY_APP_ID = "appId";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_SCOPE = "scope";
    public static final String KEY_CALLBACK_URL = "callbackUrl";
    public static final String VALUE_OAUTH_REQUEST = "create_oauth_request";

    /**
     * Get code for oauth request.
     *
     * @param oauthAppMap Oauth app map
     * @param accessToken access token
     * @return code
     */
    public String getCodeOauthRequest(Map<String, String> oauthAppMap, String accessToken) {
        RestOperations restOperation = new RestOperations();

        Map<String, Object> data = new HashMap<>();
        data.put(KEY_ACTION, VALUE_OAUTH_REQUEST);
        data.put(KEY_APP_ID, oauthAppMap.get("id"));
        data.put(KEY_USER_ID, extractUserId(accessToken));
        data.put(KEY_SCOPE, Collections.singletonList(buildScope(buildCondition())));
        data.put(KEY_CALLBACK_URL, CommonUtils.getValueFromArrayString((String) oauthAppMap.get("callbackUrls")));

        ResponseEntity<Map<String, String>> responseEntity = restOperation.getForMap(identityUrl, HttpMethods.POST, accessToken, String.class, data);
        Map<String, String> result = CommonUtils.getResponseObject(responseEntity);

        if (!CommonUtils.isEmpty(result)) {
            return result.get("code");
        } else {
            return null;
        }
    }

    /**
     * Build Scope instance.
     *
     * @return {@link Scope} instance
     */
    private Scope buildScope(Condition condition) {
        Scope scope = new Scope();
        scope.setName("user.read");
        scope.setCondition(condition);
        return scope;
    }

    /**
     * Build condition instance.
     *
     * @return {@link Condition} instance
     */
    private Condition buildCondition() {
        Condition condition = new Condition();
        condition.setSubset(Arrays.asList("id", "username", "profilePhoto", "branchId", "branch"));
        return condition;
    }

    /**
     * Extract user id from access token.
     *
     * @param accessToken the access token contains payload data
     * @return the user id
     */
    private int extractUserId(String accessToken) {
        // Get user id from access token
        String payloadToken = accessToken.substring(0, accessToken.lastIndexOf(Constants.DOT) + 1);
        Jwt<Header, Claims> untrusted = Jwts.parserBuilder().build().parseClaimsJwt(payloadToken);
        return untrusted.getBody().get("userId", Integer.class);
    }
}
