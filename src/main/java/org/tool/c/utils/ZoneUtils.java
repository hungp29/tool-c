package org.tool.c.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Zone Utils.
 *
 * @author hungp
 */
public class ZoneUtils {

    /**
     * Prevents new instance.
     */
    private ZoneUtils() {
    }

    /**
     * Convert to timezone.
     *
     * @param time   time
     * @param zoneId zone id
     * @return time after converted
     */
    public static LocalDateTime convertToTimeZone(LocalDateTime time, String zoneId) {
        ZonedDateTime zonedUTC = time.atZone(ZoneId.of("UTC"));
        ZonedDateTime zonedIST = zonedUTC.withZoneSameInstant(ZoneId.of(zoneId));
        return zonedIST.toLocalDateTime();
    }
}
