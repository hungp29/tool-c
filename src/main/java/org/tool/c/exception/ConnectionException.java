package org.tool.c.exception;

/**
 * Requeset Connection Exception.
 */
public class ConnectionException extends RuntimeException {

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(Throwable thr) {
        super(thr);
    }

    public ConnectionException(String message, Throwable thr) {
        super(message, thr);
    }
}
