package org.tool.c.utils;

import org.tool.c.exception.ErrorResponseException;
import org.tool.c.services.http.ResponseEntity;

import java.util.Map;

/**
 * Common Utils.
 */
public class CommonUtils {

    /**
     * Private constructor to prevent new instance of CommonUtils.
     */
    private CommonUtils() {
    }

    /**
     * Check string is empty or not.
     *
     * @param data string value
     * @return true if string is empty, otherwise return false
     */
    public static boolean isEmpty(String data) {
        return null == data || data.isEmpty();
    }

    /**
     * Check object is empty or not.
     *
     * @param data object to check
     * @return true if object is null, otherwise return false
     */
    public static boolean isEmpty(Object data) {
        return null == data;
    }

    /**
     * Check map is empty or not.
     *
     * @param data map to check
     * @return true if map is empty, otherwise return false
     */
    public static boolean isEmpty(Map data) {
        return null == data || data.isEmpty();
    }

    /**
     * Get object from response entity.
     *
     * @param responseEntity response entity
     * @param <T>            wildcard of object
     * @return object get from response entity
     */
    public static <T> T getResponseObject(ResponseEntity<T> responseEntity) {
        if (responseEntity.isStatus()) {
            return responseEntity.getObject();
        } else {
            throw new ErrorResponseException();
        }
    }

    /**
     * Get status of response entity.
     *
     * @param responseEntity response entity
     * @return status of response entity
     */
    public static boolean getReponseStatus(ResponseEntity responseEntity) {
        return responseEntity.isStatus();
    }

    /**
     * Get value from array string.
     *
     * @param arrayString array string
     * @return value
     */
    public static String getValueFromArrayString(String arrayString) {
        return arrayString.replace("[", "").replace("]", "").replace("\"", "");
    }
}
