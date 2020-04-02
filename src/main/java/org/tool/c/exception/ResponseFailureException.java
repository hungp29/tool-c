package org.tool.c.exception;

/**
 * Error Response Exception.
 */
public class ResponseFailureException extends RuntimeException {

    public static final String MESSAGE = "Response is failure";

    public ResponseFailureException() {
        super(MESSAGE);
    }

    public ResponseFailureException(String message) {
        super(message);
    }

    public ResponseFailureException(Throwable err) {
        super(MESSAGE, err);
    }

    public ResponseFailureException(String message, Throwable err) {
        super(message, err);
    }
}
