package org.tool.c.exception;

/**
 * Extract Response Exception.
 */
public class ExtractResponseException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "An error occurs while extracting data from response";

    public ExtractResponseException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public ExtractResponseException(Throwable thr) {
        super(DEFAULT_ERROR_MESSAGE, thr);
    }
}
