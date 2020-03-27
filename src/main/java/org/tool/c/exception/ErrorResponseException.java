package org.tool.c.exception;

/**
 * Error Response Exception.
 */
public class ErrorResponseException extends RuntimeException {

    public static final String MESSAGE = "Request returned false";

    public ErrorResponseException() {
        super(MESSAGE);
    }

    public ErrorResponseException(Throwable err) {
        super(MESSAGE, err);
    }

    public ErrorResponseException(String message, Throwable err) {
        super(message, err);
    }
}
