package org.tool.c.exception;

/**
 * Request Input/Output Stream Exception.
 */
public class RequestIOStreamException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "Error occurs while creating the input/output stream";

    public RequestIOStreamException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public RequestIOStreamException(String message) {
        super(message);
    }

    public RequestIOStreamException(Throwable thr) {
        super(DEFAULT_ERROR_MESSAGE, thr);
    }
}
