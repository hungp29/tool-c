package org.tool.c.app.action;

import org.tool.c.bundle.AppBundle;
import org.tool.c.services.http.HttpMethods;
import org.tool.c.services.http.ResponseEntity;
import org.tool.c.services.http.RestOperations;
import org.tool.c.utils.CommonUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OAuth Action. It use to get OAuth.
 *
 * @author hungp
 */
public class OAuthAction extends AppBundle {

    public static final String KEY_ACTION = "action";
    public static final String KEY_ID = "id";
    public static final String KEY_INFO = "info";
    public static final String VALUE_ACTION = "get_oauth_app";
    public static final List<String> VALUE_INFO = Arrays.asList("id", "name", "callbackUrls");

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
        data.put(KEY_ACTION, VALUE_ACTION);
        data.put(KEY_ID, 1);
        data.put(KEY_INFO, VALUE_INFO);

        // Send request to get response
        ResponseEntity<Map<String, String>> responseEntity = restOperation.getForMap(identityUrl, HttpMethods.POST, accessToken, String.class, data);

        return CommonUtils.getResponseObject(responseEntity);
    }
}
