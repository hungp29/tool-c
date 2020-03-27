package org.tool.c.services.http;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Rest Operation.
 */
public class RestOperations {


    /**
     * Call API and get result as object.
     *
     * @param url           url
     * @param httpMethod    http method
     * @param responseClass object class
     * @param data          data of request
     * @param <T>           class of object
     * @return object
     * @throws IOException
     */
    public <T> ResponseEntity<T> getForObject(String url, HttpMethods httpMethod, Class<T> responseClass, Map<String, ?> data) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponse(request.getResponseString(), responseClass);
    }

    public boolean getResponseStatus(String url, HttpHeaders headers, HttpMethods httpMethod, Map<String, ?> data) throws IOException {
        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        String res = request.getResponseString();
        return false;
    }

    protected <T> ResponseEntity<T> extractResponse(String response, Class<T> responseClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map mapResponse = mapper.readValue(response, Map.class);
        boolean status = (boolean) mapResponse.get("success");

        if (status) {
            T object = mapper.convertValue(mapResponse.get("result"), responseClass);
            return new ResponseEntity<T>(status, object);
        } else {
            ErrorResponse object = mapper.convertValue(mapResponse.get("error"), ErrorResponse.class);
//            return new ResponseEntity<ErrorResponse>(status, object);
            return null;
        }
    }
}
