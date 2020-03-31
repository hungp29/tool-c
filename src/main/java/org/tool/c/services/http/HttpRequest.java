package org.tool.c.services.http;

import org.json.JSONObject;
import org.tool.c.exception.*;
import org.tool.c.utils.CommonUtils;
import org.tool.c.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * Http Request class.
 */
public class HttpRequest {

    /**
     * Http URL connection.
     */
    HttpURLConnection conn;

    /**
     * Constructor to new Http URL connection.
     *
     * @param url url
     */
    public HttpRequest(String url) {
        // https://stackoverflow.com/questions/33084855/way-to-ignore-ssl-certificate-using-httpsurlconnection
        // https://stackoverflow.com/questions/33067368/okhttp-trusting-certificate
        // https://nakov.com/blog/2009/07/16/disable-certificate-validation-in-java-ssl-connections/
        try {
            HttpsTrustManager.allowAllSSL();
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
        } catch (IOException e) {
            throw new RequestConnectionException(url, e);
        }
    }

    /**
     * Constructor to new Http URL Connection with request method.
     *
     * @param url           url
     * @param requestMethod request method
     */
    public HttpRequest(String url, HttpMethods requestMethod) {
        this(url);

        try {
            conn.setRequestMethod(requestMethod.getCode());
        } catch (ProtocolException e) {
            throw new RequestMethodException(requestMethod.getCode(), e);
        }
    }

    /**
     * Sets headers to Http URL Connection.
     *
     * @param httpHeaders Http Headers
     * @return Http URL Connection
     */
    public HttpURLConnection setHeaders(HttpHeaders httpHeaders) {
        if (!CommonUtils.isEmpty(httpHeaders)) {
            Set<String> keys = httpHeaders.keySet();
            for (String key : keys) {
                httpHeaders.get(key).forEach(value -> conn.setRequestProperty(key, value));
            }
        }
        return conn;
    }

    /**
     * Set Connect timeout.
     *
     * @param timeInMilliseconds time in milliseconds
     * @return Http URL Connection
     */
    public HttpURLConnection setConnectTimeout(int timeInMilliseconds) {
        conn.setConnectTimeout(timeInMilliseconds);
        return conn;
    }

    /**
     * Set Read timeout.
     *
     * @param timeInMilliseconds time in milliseconds
     * @return Http URL Connection
     */
    public HttpURLConnection setReadTimeout(int timeInMilliseconds) {
        conn.setReadTimeout(timeInMilliseconds);
        return conn;
    }

    /**
     * Get Http URL Connection.
     *
     * @return Http URL Connection
     */
    public HttpURLConnection getConnection() {
        return conn;
    }

    /**
     * Disconnect.
     */
    public void disconnect() {
        conn.disconnect();
    }

    /**
     * Get response code.
     *
     * @return response code
     */
    public int getResponseCode() {
        try {
            return conn.getResponseCode();
        } catch (IOException e) {
            throw new ResponseCodeException();
        }
    }

    /**
     * Get output stream.
     *
     * @return output stream
     */
    public OutputStream getOutputStream() {
        try {
            return conn.getOutputStream();
        } catch (IOException e) {
            throw new RequestIOStreamException(e);
        }
    }

    /**
     * Get input stream.
     *
     * @return input stream
     */
    public InputStream getInputStream() {
        try {
            return conn.getInputStream();
        } catch (IOException e) {
            throw new RequestIOStreamException(e);
        }
    }

    /**
     * Get error stream.
     *
     * @return error stream
     */
    public InputStream getErrorStream() {
        return conn.getErrorStream();
    }

    /**
     * Get Response as string.
     *
     * @return response
     */
    public String getResponseString() {
        if (getResponseCode() > 299) {
            return FileUtils.readInputStream(getErrorStream());
        } else {
            return FileUtils.readInputStream(getInputStream());
        }
    }

    /**
     * Put data to request and send it.
     *
     * @param data data
     */
    public void send(Map<String, ?> data) {
        try {
            JSONObject object = new JSONObject(data);
            OutputStream os = getOutputStream();
            os.write(object.toString().getBytes());
            os.flush();
        } catch (IOException e) {
            throw new RequestSendDataException(e);
        }
    }
}
