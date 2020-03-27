package org.tool.c.services.http;

/**
 * Http Methods enum.
 */
public enum HttpMethods {
    POST("POST"),
    GET("GET");

    private String code;

    HttpMethods(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
