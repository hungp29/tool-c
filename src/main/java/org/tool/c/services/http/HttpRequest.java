package org.tool.c.services.http;

import org.tool.c.utils.CommonUtils;
import org.tool.c.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

/**
 * Http Request class.
 */
public class HttpRequest {

    /**
     * Http URL connection.
     */
    HttpURLConnection conn = null;

    /**
     * Constructor to new Http URL connection.
     *
     * @param url url
     */
    public HttpRequest(String url) throws IOException {
        // https://stackoverflow.com/questions/33084855/way-to-ignore-ssl-certificate-using-httpsurlconnection
        // https://stackoverflow.com/questions/33067368/okhttp-trusting-certificate
        // https://nakov.com/blog/2009/07/16/disable-certificate-validation-in-java-ssl-connections/
        HttpsTrustManager.allowAllSSL();
        conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoOutput(true);
    }

    /**
     * Constructor to new Http URL Connection with request method.
     *
     * @param url           url
     * @param requestMethod request method
     * @throws IOException
     */
    public HttpRequest(String url, HttpMethods requestMethod) throws IOException {
        this(url);
        conn.setRequestMethod(requestMethod.getCode());
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
     * @throws IOException
     */
    public int getResponseCode() throws IOException {
        return conn.getResponseCode();
    }

    /**
     * Get output stream.
     *
     * @return output stream
     * @throws IOException
     */
    public OutputStream getOutputStream() throws IOException {
        return conn.getOutputStream();
    }

    /**
     * Get input stream.
     *
     * @return input stream
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException {
        return conn.getInputStream();
    }

    /**
     * Get error stream.
     *
     * @return error stream
     * @throws IOException
     */
    public InputStream getErrorStream() throws IOException {
        return conn.getErrorStream();
    }

    /**
     * Get Response.
     *
     * @return response
     * @throws IOException
     */
    public String getResponse() throws IOException {
        if (conn.getResponseCode() > 299) {
            return FileUtils.readInputStream(conn.getErrorStream());
        } else {
            return FileUtils.readInputStream(conn.getInputStream());
        }
    }
}
