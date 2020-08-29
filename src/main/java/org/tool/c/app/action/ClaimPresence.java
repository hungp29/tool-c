package org.tool.c.app.action;

import org.tool.c.app.entity.TimeSheet;
import org.tool.c.base.Base;
import org.tool.c.services.http.HttpMethods;
import org.tool.c.services.http.ResponseEntity;
import org.tool.c.services.http.RestOperations;
import org.tool.c.utils.CommonUtils;
import org.tool.c.utils.constants.Actions;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Claim for Presence.
 */
public class ClaimPresence extends Base {

    /**
     * Get personal time sheet.
     *
     * @param accessToken access token for checkin app
     * @return list time sheet
     */
    public List<TimeSheet> getPersonalTimeSheet(String accessToken) {
        RestOperations restOperation = new RestOperations();
        Map<String, Object> data = new HashMap<>();
        data.put("action_name", Actions.PERSONAL_TIMESHEET);

        ResponseEntity<List<TimeSheet>> responseEntity = restOperation.getForListObject(checkinUrl, HttpMethods.POST, accessToken, "time_sheet", TimeSheet.class, data);
        return CommonUtils.getResponseObject(responseEntity);
    }

    /**
     * Get Current time sheet.
     *
     * @param timeSheets list time sheet
     * @return current time sheet
     */
    public TimeSheet getCurrentTimeSheet(List<TimeSheet> timeSheets) {
        return timeSheets.stream().
                filter(timeSheet -> timeSheet.getWorkDay().isEqual(LocalDate.now()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Claim for presence.
     *
     * @param accessToken Access token for checkin app
     * @return TimeSheet if claim is successfully
     */
    public TimeSheet claim(String accessToken) {
        RestOperations restOperation = new RestOperations();
        Map<String, Object> data = new HashMap<>();
        data.put("action_name", Actions.CLAIM_FOR_PRESENCE);

        ResponseEntity<TimeSheet> responseEntity = restOperation.getForObject(checkinUrl, HttpMethods.POST, accessToken, "checked", TimeSheet.class, data);

        return CommonUtils.getResponseObject(responseEntity);
    }

    /**
     * Check current time is out of working time.
     *
     * @return true if it's out of working time
     */
    public boolean checkIsOutWorkTime() {
        LocalTime endWorkTime = LocalTime.parse(bundle.getString("working.time.afternoon.end"));
        return LocalTime.now().isAfter(endWorkTime);
    }

    /**
     * Check status of checkout.
     *
     * @param timeSheet current time sheet
     * @return true if we have checkout
     */
    public boolean isCheckOut(TimeSheet timeSheet) {
        LocalTime endWorkTime = LocalTime.parse(bundle.getString("working.time.afternoon.end"));
        LocalDateTime dateTimeEnd = LocalDateTime.of(LocalDate.now(), endWorkTime);
        return !CommonUtils.isEmpty(timeSheet) && !CommonUtils.isEmpty(timeSheet.getCheckOutTime()) && !timeSheet.getCheckOutTime().isBefore(dateTimeEnd);
    }

    /**
     * Calculate late time.
     *
     * @param timeSheets list timesheet
     * @return total late time
     */
    public int calcLateTime(List<TimeSheet> timeSheets) {
        int late = 0;
        if (null != timeSheets && timeSheets.size() > 0) {
            int month = LocalDate.now().getMonthValue();
            int year = LocalDate.now().getYear();
            LocalTime startMorningWorkTime = LocalTime.parse(bundle.getString("working.time.morning.start"));
            LocalTime endAfternoonWorkTime = LocalTime.parse(bundle.getString("working.time.afternoon.end"));

            late = timeSheets.parallelStream()
                    .filter(timeSheet -> timeSheet.getWorkDay().getMonthValue() == month &&
                            timeSheet.getWorkDay().getYear() == year &&
                            timeSheet.getWorkDay().getDayOfWeek().getValue() != DayOfWeek.SATURDAY.getValue() &&
                            timeSheet.getWorkDay().getDayOfWeek().getValue() != DayOfWeek.SUNDAY.getValue())
                    .mapToInt(value -> {
                        int timeLate = 0;
                        LocalDateTime startTime = CommonUtils.convertToSystemTimeZone(value.getCheckInTime());
                        LocalDateTime endTime = CommonUtils.convertToSystemTimeZone(value.getCheckOutTime());

                        int lateAtStart = (int) Math.ceil((float) ChronoUnit.SECONDS.between(startMorningWorkTime, startTime.toLocalTime()) / 60);
                        if (lateAtStart > 0) {
                            timeLate += lateAtStart;
                        }

                        if (!endTime.isEqual(startTime) && endTime.toLocalTime().isAfter(LocalTime.of(17, 0))) {
                            int lateAtEnd = (int) Math.ceil((float) ChronoUnit.SECONDS.between(endTime.toLocalTime(), endAfternoonWorkTime) / 60);
                            if (lateAtEnd > 0) {
                                timeLate += lateAtEnd;
                            }
                        }
                        return timeLate;
                    }).sum();
        }
        return late;
    }
}
