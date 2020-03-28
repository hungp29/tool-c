package org.tool.c.services.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.tool.c.exception.ErrorResponseException;

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

    /**
     * Call API and get result as object.
     *
     * @param url           url
     * @param httpMethod    http method
     * @param accessToken   access token value
     * @param responseClass object class
     * @param data          data of request
     * @param <T>           class of object
     * @return object
     * @throws IOException
     */
    public <T> ResponseEntity<T> getForObject(String url, HttpMethods httpMethod, String accessToken, Class<T> responseClass, Map<String, ?> data) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));
        headers.put(HttpHeaders.Authorization, Collections.singletonList(accessToken));

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponse(request.getResponseString(), responseClass);
    }

    /**
     * Call API and get result as object.
     *
     * @param url           url
     * @param httpMethod    http method
     * @param headers       the headers of request
     * @param responseClass object class
     * @param data          data of request
     * @param <T>           class of object
     * @return object
     * @throws IOException
     */
    public <T> ResponseEntity<T> getForObject(String url, HttpMethods httpMethod, HttpHeaders headers, Class<T> responseClass, Map<String, ?> data) throws IOException {
        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponse(request.getResponseString(), responseClass);
    }

//    /**
//     * Get Response status.
//     *
//     * @param url         url
//     * @param accessToken access token value
//     * @param httpMethod  http method
//     * @param data        data of request
//     * @return status of response
//     * @throws IOException
//     */
//    public boolean getResponseStatus(String url, String accessToken, HttpMethods httpMethod, Map<String, ?> data) throws IOException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));
//        headers.put(HttpHeaders.Authorization, Collections.singletonList(accessToken);
//
//        HttpRequest request = new HttpRequest(url, httpMethod);
//        request.setHeaders(headers);
//        request.send(data);
//
//        ResponseEntity<Map> responseEntity = extractResponseStatus(request.getResponseString());
//        return responseEntity.isStatus();
//    }
//
//    public boolean getResponseStatus(String url, HttpHeaders headers, HttpMethods httpMethod, Map<String, ?> data) throws IOException {
//        HttpRequest request = new HttpRequest(url, httpMethod);
//        request.setHeaders(headers);
//        request.send(data);
//
//        ResponseEntity<Map> responseEntity = extractResponseStatus(request.getResponseString());
//        return responseEntity.isStatus();
//    }
//
//    protected ResponseEntity<Map> extractResponseStatus(String response) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        Map mapResponse = mapper.readValue(response, Map.class);
//        boolean status = (boolean) mapResponse.get("success");
//
//        if (status) {
//            Map object = mapper.convertValue(mapResponse.get("result"), Map.class);
//            return new ResponseEntity<Map>(status, object);
//        } else {
//            ErrorResponse object = mapper.convertValue(mapResponse.get("error"), ErrorResponse.class);
//            throw new ErrorResponseException(object.getCode());
//        }
//    }

    protected <T> ResponseEntity<T> extractResponse(String response, Class<T> responseClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map mapResponse = mapper.readValue(response, Map.class);
        boolean status = (boolean) mapResponse.get("success");

        if (status) {
            T object = mapper.convertValue(mapResponse.get("result"), responseClass);
            return new ResponseEntity<T>(status, object);
        } else {
            ErrorResponse object = mapper.convertValue(mapResponse.get("error"), ErrorResponse.class);
            throw new ErrorResponseException(object.getCode());
        }
    }
}
