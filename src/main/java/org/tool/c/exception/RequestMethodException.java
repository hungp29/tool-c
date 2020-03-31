package org.tool.c.exception;

import org.tool.c.utils.CommonUtils;

/**
 * Request Method Exception.
 */
public class RequestMethodException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "The method {0} is not supported";

    public RequestMethodException(String method) {
        super(CommonUtils.replace(DEFAULT_ERROR_MESSAGE, method));
    }

    public RequestMethodException(Throwable thr) {
        super(thr);
    }

    public RequestMethodException(String method, Throwable thr) {
        super(CommonUtils.replace(DEFAULT_ERROR_MESSAGE, method), thr);
    }

}
