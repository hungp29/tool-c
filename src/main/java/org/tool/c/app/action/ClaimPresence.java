package org.tool.c.app.action;

import org.tool.c.app.entity.TimeSheet;
import org.tool.c.base.Base;
import org.tool.c.services.http.HttpMethods;
import org.tool.c.services.http.ResponseEntity;
import org.tool.c.services.http.RestOperations;
import org.tool.c.utils.CommonUtils;
import org.tool.c.utils.constants.Actions;

import java.time.LocalDate;
import java.time.LocalTime;
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
     * @param timeSheet current timesheet
     * @return true if it's out of working time
     */
    public boolean checkIsOutWorkTime(TimeSheet timeSheet) {
        LocalTime endWorkTime = LocalTime.parse(bundle.getString("working.time.afternoon.end"));
        return LocalTime.now().isAfter(endWorkTime);
    }
}
