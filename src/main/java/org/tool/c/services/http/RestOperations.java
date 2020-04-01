package org.tool.c.services.http;

import org.tool.c.exception.ConverterUnsupportedJavaTypeException;
import org.tool.c.exception.ErrorResponseException;
import org.tool.c.services.http.converter.JacksonConverter;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Rest Operation.
 */
public class RestOperations {

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

        return extractResponse(request.getInputStream(), responseClass, null);
    }

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
        HttpHeaders headers = HttpHeaders.newInstance("application/json");

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponse(request.getInputStream(), responseClass, null);
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
        HttpHeaders headers = HttpHeaders.newInstanceAuth("application/json", accessToken);

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponse(request.getInputStream(), responseClass, null);
    }

    /**
     * Call API and get result as object.
     *
     * @param url           url
     * @param httpMethod    http method
     * @param accessToken   access token value
     * @param fieldPath     path to get object
     * @param responseClass object class
     * @param data          data of request
     * @param <T>           class of object
     * @return object
     */
    public <T> ResponseEntity<T> getForObject(String url, HttpMethods httpMethod, String accessToken, String fieldPath, Class<T> responseClass, Map<String, ?> data) {
        HttpHeaders headers = HttpHeaders.newInstanceAuth("application/json", accessToken);

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponse(request.getInputStream(), responseClass, fieldPath);
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
        HttpHeaders headers = HttpHeaders.newInstance("application/json");

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponseAsList(request.getInputStream(), responseClass);
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
        HttpHeaders headers = HttpHeaders.newInstanceAuth("application/json", accessToken);

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponseAsList(request.getInputStream(), responseClass);
    }

    /**
     * Call API and get result as Map.
     *
     * @param url        utl
     * @param httpMethod http method
     * @param data       data
     * @return Map data
     */
    public <T> ResponseEntity<Map<String, T>> getForMap(String url, HttpMethods httpMethod, Class<T> responseClass, Map<String, ?> data) {
        HttpHeaders headers = HttpHeaders.newInstance("application/json");

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponseAsMap(request.getInputStream(), responseClass);
    }

    /**
     * Call API and get result as Map.
     *
     * @param url         utl
     * @param httpMethod  http method
     * @param accessToken Access token
     * @param data        data
     * @return Map data
     */
    public <T> ResponseEntity<Map<String, T>> getForMap(String url, HttpMethods httpMethod, String accessToken, Class<T> responseClass, Map<String, ?> data) {
        HttpHeaders headers = HttpHeaders.newInstanceAuth("application/json", accessToken);

        HttpRequest request = new HttpRequest(url, httpMethod);
        request.setHeaders(headers);
        request.send(data);

        return extractResponseAsMap(request.getInputStream(), responseClass);
    }

    /**
     * Extract response to ResponseEntity.
     *
     * @param response      InputStream response
     * @param responseClass class of response object
     * @param path          path to get object
     * @param <T>           wildcard for response class
     * @return response entity for object
     */
    @SuppressWarnings("unchecked")
    protected <T> ResponseEntity<T> extractResponse(InputStream response, Class<T> responseClass, String path) {
        JacksonConverter converter = new JacksonConverter();
        if (converter.support(responseClass)) {
            AtomicReference<Throwable> cause = new AtomicReference<>();
            T result = (T) converter.read(responseClass, response, cause, path);

            Throwable thr = cause.get();
            if (null != thr) {
                throw new ErrorResponseException(thr);
            }
            return new ResponseEntity<>(true, result);
        } else {
            throw new ConverterUnsupportedJavaTypeException("Converter do not support class " + responseClass.getName());
        }
    }

    /**
     * Extract response to ResponseEntity with List object.
     *
     * @param response      Input Stream of request
     * @param responseClass class of object
     * @param <T>           wildcard for class
     * @return list object
     */
    @SuppressWarnings("unchecked")
    protected <T> ResponseEntity<List<T>> extractResponseAsList(InputStream response, Class<T> responseClass) {
        JacksonConverter converter = new JacksonConverter();
        if (converter.support(responseClass)) {
            AtomicReference<Throwable> cause = new AtomicReference<>();
            List<T> result = (List<T>) converter.readAsList(responseClass, response, cause);

            Throwable thr = cause.get();
            if (null != thr) {
                throw new ErrorResponseException(thr);
            }
            return new ResponseEntity<>(true, result);
        } else {
            throw new ConverterUnsupportedJavaTypeException("Converter do not support class " + responseClass.getName());
        }
    }

    /**
     * Extract repsonse as Map.
     *
     * @param response response
     * @return Map data
     */
    @SuppressWarnings("unchecked")
    protected <T> ResponseEntity<Map<String, T>> extractResponseAsMap(InputStream response, Class<T> responseClass) {
        JacksonConverter converter = new JacksonConverter();
        if (converter.support(responseClass)) {
            AtomicReference<Throwable> cause = new AtomicReference<>();
            Map<String, T> result = (Map<String, T>) converter.readAsMap(responseClass, response, cause);

            Throwable thr = cause.get();
            if (null != thr) {
                throw new ErrorResponseException(thr);
            }
            return new ResponseEntity<>(true, result);
        } else {
            throw new ConverterUnsupportedJavaTypeException("Converter do not support class " + responseClass.getName());
        }
    }
}
