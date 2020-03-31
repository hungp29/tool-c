package org.tool.c.services.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.tool.c.exception.ErrorResponseException;
import org.tool.c.exception.ExtractResponseException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     */
    public <T> ResponseEntity<T> getForObject(String url, HttpMethods httpMethod, Class<T> responseClass, Map<String, ?> data) {
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
     */
    public <T> ResponseEntity<T> getForObject(String url, HttpMethods httpMethod, String accessToken, Class<T> responseClass, Map<String, ?> data) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));
        headers.put(HttpHeaders.Authorization, Collections.singletonList(accessToken));

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponse(request.getResponseString(), responseClass);
    }

    public ResponseEntity<Map<String, ?>> getForObjectAsMap(String url, HttpMethods httpMethod, String accessToken, Map<String, ?> data) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));
        headers.put(HttpHeaders.Authorization, Collections.singletonList(accessToken));

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

//        return extractResponse(request.getResponseString(), Map.cl);
        return null;
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
     */
    public <T> ResponseEntity<T> getForObject(String url, HttpMethods httpMethod, HttpHeaders headers, Class<T> responseClass, Map<String, ?> data) {
        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponse(request.getResponseString(), responseClass);
    }

    /**
     * Call API and get result as list object.
     *
     * @param url           url
     * @param httpMethod    http method
     * @param responseClass class of object
     * @param data          data of request
     * @param <T>           class type
     * @return list object has specify class type
     */
    public <T> ResponseEntity<List<T>> getForListObject(String url, HttpMethods httpMethod, Class<T> responseClass, Map<String, ?> data) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponseAsList(request.getResponseString(), responseClass);
    }

    /**
     * Call API and get result as list object.
     *
     * @param url           url
     * @param httpMethod    http method
     * @param accessToken   Access token
     * @param responseClass class of object
     * @param data          data of request
     * @param <T>           class type
     * @return list object has specify class type
     */
    public <T> ResponseEntity<List<T>> getForListObject(String url, HttpMethods httpMethod, String accessToken, Class<T> responseClass, Map<String, ?> data) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));
        headers.put(HttpHeaders.Authorization, Collections.singletonList(accessToken));

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponseAsList(request.getResponseString(), responseClass);
    }

    /**
     * Extract response to ResponseEntity.
     *
     * @param response
     * @param responseClass
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> extractResponse(String response, Class<T> responseClass) {
        try {
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
        } catch (IOException e) {
            throw new ExtractResponseException(e);
        }
    }

    /**
     * Extract response to ResponseEntity with List object.
     *
     * @param response
     * @param responseClass
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<List<T>> extractResponseAsList(String response, Class<T> responseClass) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            Map mapResponse = mapper.readValue(response, Map.class);
            boolean status = (boolean) mapResponse.get("success");

            if (status) {
                Object result = mapResponse.get("result");
                List<T> lstObject = null;
                if (result instanceof List) {
                    lstObject = (List<T>) ((List) result).stream().map(obj -> mapper.convertValue(obj, responseClass)).collect(Collectors.toList());
                } else if (result instanceof Map) {
                    Map<String, List> mapResult = (Map<String, List>) result;
                    if (mapResult.size() == 1) {
                        lstObject = (List<T>) mapResult.values().stream().flatMap(coll -> coll.stream()).map(obj -> mapper.convertValue(obj, responseClass)).collect(Collectors.toList());
                    }
                }
                return new ResponseEntity<>(status, lstObject);
            } else {
                ErrorResponse object = mapper.convertValue(mapResponse.get("error"), ErrorResponse.class);
                throw new ErrorResponseException(object.getCode());
            }
        } catch (IOException e) {
            throw new ExtractResponseException(e);
        }
    }
}
