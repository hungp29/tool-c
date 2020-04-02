package org.tool.c.exception;

/**
 * Read data input stream exception.
 */
public class ReadInputStreamException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "An error occurs while reading data from stream";

    public ReadInputStreamException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public ReadInputStreamException(Throwable thr) {
        super(DEFAULT_ERROR_MESSAGE, thr);
    }
}
