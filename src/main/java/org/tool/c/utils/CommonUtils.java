package org.tool.c.utils;

import org.tool.c.exception.ResponseFailureException;
import org.tool.c.services.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
            throw new ResponseFailureException();
        }
    }

    /**
     * Get status of response entity.
     *
     * @param responseEntity response entity
     * @return status of response entity
     */
    public static boolean getResponseStatus(ResponseEntity responseEntity) {
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

    /**
     * Convert date time to system timezone.
     *
     * @param dateTime date time need to convert
     * @return date time has converted
     */
    public static LocalDateTime convertToSystemTimeZone(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Replace string by position.
     *
     * @param message pattern
     * @param values  values
     * @return string
     */
    public static String replace(String message, String... values) {
        int index = 0;
        for (String value : values) {
            message = message.replaceAll("\\{" + index++ + "\\}", value);
        }
        return message;
    }

    /**
     * Trim value string.
     *
     * @param value value need to trim
     * @return value has trim
     */
    public static String trim(String value) {
        return isEmpty(value) ? "" : value.trim();
    }
}
