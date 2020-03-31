package org.tool.c.exception;

/**
 * Requeset Connection Exception.
 */
public class RequestConnectionException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "Cannot open connection to URL";

    public RequestConnectionException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public RequestConnectionException(String url) {
        super(DEFAULT_ERROR_MESSAGE + ": " + url);
    }

    public RequestConnectionException(Throwable thr) {
        super(thr);
    }

    public RequestConnectionException(String url, Throwable thr) {
        super(DEFAULT_ERROR_MESSAGE + ": " + url, thr);
    }
}
