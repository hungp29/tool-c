package org.tool.c.exception;

/**
 * Response Template Unsupported Exception.
 */
public class ResponseTemplateUnsupportedException extends RuntimeException {

    public ResponseTemplateUnsupportedException(String message) {
        super(message);
    }

    public ResponseTemplateUnsupportedException(String message, Throwable thr) {
        super(message, thr);
    }
}
