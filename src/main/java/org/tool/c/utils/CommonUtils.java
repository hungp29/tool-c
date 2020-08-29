package org.tool.c.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.exception.ResponseFailureException;
import org.tool.c.services.http.ResponseEntity;
import org.tool.c.utils.constants.Constants;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Common Utils.
 */
public class CommonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CommonUtils.class);

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

    /**
     * Sleep thread.
     *
     * @param timeInMillis time in millis
     */
    public static void sleep(long timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            LOG.error("Cannot sleep thread", e);
        }
    }

    /**
     * Format LocalDateTime to String.
     *
     * @param time time need to format
     * @return time as string
     */
    public static String formatLocalDateTime(LocalDateTime time) {
        return formatLocalDateTime(time, Constants.DATE_TIME_FM);
    }

    /**
     * Format LocalDateTime to String.
     *
     * @param time   time need to format
     * @param format format pattern
     * @return time as string
     */
    public static String formatLocalDateTime(LocalDateTime time, String format) {
        String timeFormatted = Constants.EMPTY;
        if (!isEmpty(time)) {
            timeFormatted = time.format(DateTimeFormatter.ofPattern(format));
        }
        return timeFormatted;
    }

    /**
     * Format LocalDate to String.
     *
     * @param date date need to format
     * @return time as string
     */
    public static String formatLocalDate(LocalDate date) {
        String dateFormatted = Constants.EMPTY;
        if (!isEmpty(date)) {
            dateFormatted = date.format(DateTimeFormatter.ofPattern(Constants.DATE_FM));
        }
        return dateFormatted;
    }

    /**
     * Format number with 2 digits after dot.
     *
     * @param number number need to format
     * @return number formatted
     */
    public static String formatNumber(float number) {
        NumberFormat format = new DecimalFormat("0.00");
        return format.format(number);
    }

    /**
     * Format hours to HH:mm.
     *
     * @param number hours
     * @return hours formatted
     */
    public static String formatHour(float number) {
        NumberFormat format = new DecimalFormat("00");
        int hours = (int) number;
        int minutes = (int) ((number - hours) * 60);
        return format.format(hours) + ":" + format.format(minutes);
    }
}
