package org.tool.c.app;

import org.tool.c.services.http.HttpHeaders;
import org.tool.c.services.http.HttpMethods;
import org.tool.c.services.http.HttpRequest;
import org.tool.c.services.http.RestOperations;
import org.tool.c.utils.constants.Constants;

import java.io.IOException;
import java.util.*;

/**
 * Authentication class.
 */
public class Authentication {

    /**
     * Authentication by username and password.
     *
     * @param username username
     * @param password password
     * @return token
     */
    public String authenticate(String username, String password) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        String url = bundle.getString("tool.url.identity");

        RestOperations restOperation = new RestOperations();
        Map<String, Object> data = new HashMap<>();
        data.put("action", "get_oauth_token");
        data.put("grantType", "password");
        data.put("grantData", Arrays.asList("hungp@bap.jp", "112qwaszx!"));
        restOperation.getForObject(url, HttpMethods.POST, String.class, data);
        return "";
    }
}
