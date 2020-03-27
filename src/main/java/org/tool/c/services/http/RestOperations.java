package org.tool.c.services.http;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

/**
 * Rest Operation.
 */
public class RestOperations {

    public <T> T getForObject(String url, HttpMethods httpMethod, Class<T> responseClass, Map<String, ?> data) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);

        JSONObject object = new JSONObject(data);
        OutputStream os = request.getOutputStream();
        os.write(object.toString().getBytes());
        os.flush();

        String out = request.getResponse();
        return null;
    }
}
