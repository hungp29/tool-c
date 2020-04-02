package org.tool.c.exception;

import org.tool.c.utils.CommonUtils;

/**
 * Request Method Exception.
 */
public class RequestMethodNotGrantedException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "The method {0} is not supported";

    public RequestMethodNotGrantedException(String method) {
        super(CommonUtils.replace(DEFAULT_ERROR_MESSAGE, method));
    }

    public RequestMethodNotGrantedException(Throwable thr) {
        super(thr);
    }

    public RequestMethodNotGrantedException(String method, Throwable thr) {
        super(CommonUtils.replace(DEFAULT_ERROR_MESSAGE, method), thr);
    }

}
