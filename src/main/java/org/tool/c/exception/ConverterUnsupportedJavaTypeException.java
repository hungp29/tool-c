package org.tool.c.exception;

/**
 * Converter Unsupported Java Type Exception.
 */
public class ConverterUnsupportedJavaTypeException extends RuntimeException {

    public ConverterUnsupportedJavaTypeException(String message) {
        super(message);
    }

    public ConverterUnsupportedJavaTypeException(String message, Throwable thr) {
        super(message, thr);
    }
}
