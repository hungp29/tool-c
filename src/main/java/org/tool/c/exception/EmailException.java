package org.tool.c.exception;

/**
 * Email Exception.
 */
public class EmailException extends RuntimeException {

    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable thr) {
        super(message, thr);
    }
}
