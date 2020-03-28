package org.tool.c.app.claim;

import org.tool.c.services.http.HttpHeaders;
import org.tool.c.services.http.HttpMethods;
import org.tool.c.services.http.ResponseEntity;
import org.tool.c.services.http.RestOperations;
import org.tool.c.utils.CommonUtils;
import org.tool.c.utils.constants.Actions;
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
        data.put("action_name", Actions.CLAIM_FOR_PRESENCE);

        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));
        headers.put(HttpHeaders.Authorization, Collections.singletonList("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODU0OTkwMzcsImlzcyI6ImNoZWNraW4uYmFwLmpwIiwiYXVkIjoiY2xpZW50IiwidXNlcl9pZCI6MTY4fQ.8rM6gYcJoMWbK1yO40OutCoaMetTQjuLugUtZ2qb-ec"));

        ResponseEntity<Map> responseEntity = restOperation.getForObject(url, HttpMethods.POST, accessToken, Map.class, data);
        return CommonUtils.getReponseStatus(responseEntity);
    }
}
