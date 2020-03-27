package org.tool.c.app.claim;

import org.tool.c.app.authen.Token;
import org.tool.c.services.http.HttpHeaders;
import org.tool.c.services.http.HttpMethods;
import org.tool.c.services.http.ResponseEntity;
import org.tool.c.services.http.RestOperations;
import org.tool.c.utils.constants.ActionConstants;
import org.tool.c.utils.constants.Constants;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Claim for Presence.
 */
public class ClaimPresence {

    public boolean claim(String accessToken) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        String url = bundle.getString("tool.url.checkin");

        RestOperations restOperation = new RestOperations();
        Map<String, Object> data = new HashMap<>();
        data.put("action", ActionConstants.CLAIM_FOR_PRESENCE);

        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));
        headers.put(HttpHeaders.Authorization, Collections.singletonList(accessToken));
        boolean responseStatus = restOperation.getResponseStatus(url, headers, HttpMethods.POST, data);
        return false;
    }
}
