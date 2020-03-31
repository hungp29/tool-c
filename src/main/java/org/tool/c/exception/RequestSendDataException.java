package org.tool.c.exception;

/**
 * Request Send Data Exception.
 */
public class RequestSendDataException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "An error occurs while send data to server";

    public RequestSendDataException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public RequestSendDataException(Throwable thr) {
        super(DEFAULT_ERROR_MESSAGE, thr);
    }
}
