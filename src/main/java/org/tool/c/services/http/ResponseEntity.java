package org.tool.c.services.http;

import java.io.IOException;

/**
 * Response entity.
 *
 * @param <T>
 */
public class ResponseEntity<T> {

    private boolean status;
    private T object;

    public ResponseEntity(boolean status, T object) {
        this.status = status;
        this.object = object;
    }

    public boolean isStatus() {
        return status;
    }

    public T getObject() {
        return object;
    }
}
