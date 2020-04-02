package org.tool.c.exception;

/**
 * Request Send Data Exception.
 */
public class WriteOutputStreamException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "An error occurs while send data to server";

    public WriteOutputStreamException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public WriteOutputStreamException(Throwable thr) {
        super(DEFAULT_ERROR_MESSAGE, thr);
    }
}
