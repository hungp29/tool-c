package org.tool.c.exception;

/**
 * Response Converter Exception.
 */
public class ConverterException extends RuntimeException {

    public ConverterException(String message) {
        super(message);
    }

    public ConverterException(String message, Throwable thr) {
        super(message, thr);
    }
}
