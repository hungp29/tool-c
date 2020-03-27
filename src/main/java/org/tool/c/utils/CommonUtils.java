package org.tool.c.utils;

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
}
