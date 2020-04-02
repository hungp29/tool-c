package org.tool.c.exception;

/**
 * Converter Unsupported Java Type Exception.
 */
public class ConverterUnsupportedException extends RuntimeException {

    public ConverterUnsupportedException(String message) {
        super(message);
    }

    public ConverterUnsupportedException(String message, Throwable thr) {
        super(message, thr);
    }
}
