package org.tool.c.exception;

/**
 * Response Converter Exception.
 */
public class ResponseConverterException extends RuntimeException {

    public ResponseConverterException(String message) {
        super(message);
    }

    public ResponseConverterException(String message, Throwable thr) {
        super(message, thr);
    }
}
