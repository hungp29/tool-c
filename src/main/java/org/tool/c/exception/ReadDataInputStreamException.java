package org.tool.c.exception;

/**
 * Read data input stream exception.
 */
public class ReadDataInputStreamException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "An error occurs while reading data from stream";

    public ReadDataInputStreamException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public ReadDataInputStreamException(Throwable thr) {
        super(DEFAULT_ERROR_MESSAGE, thr);
    }
}
