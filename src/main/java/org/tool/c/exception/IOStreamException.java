package org.tool.c.exception;

/**
 * Request Input/Output Stream Exception.
 */
public class IOStreamException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "Error occurs while creating the input/output stream";

    public IOStreamException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public IOStreamException(String message) {
        super(message);
    }

    public IOStreamException(Throwable thr) {
        super(DEFAULT_ERROR_MESSAGE, thr);
    }
}
