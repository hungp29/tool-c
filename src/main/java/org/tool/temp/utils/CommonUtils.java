package org.tool.temp.utils;

/**
 * Common Utils.
 */
public class CommonUtils {

    /**
     * Private contructor to prevent new instance of CommonUtils.
     */
    private CommonUtils() {
    }

    /**
     * Check string is empty or not.
     *
     * @param data string value
     * @return true if string is not empty, otherwise return false
     */
    public static boolean isEmpty(String data) {
        return null == data || data.isEmpty();
    }
}
