package org.tool.c.exception;

/**
 * Request Response Code Exception.
 */
public class ResponseCodeException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "An error occurred connecting to the server";

    public ResponseCodeException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public ResponseCodeException(String message) {
        super(message);
    }

    public ResponseCodeException(String message, Throwable thr) {
        super(message, thr);
    }
}
