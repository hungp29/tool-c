package org.tool.c.app.action;

import org.tool.c.app.entity.TimeSheet;
import org.tool.c.services.http.HttpMethods;
import org.tool.c.services.http.ResponseEntity;
import org.tool.c.services.http.RestOperations;
import org.tool.c.utils.CommonUtils;
import org.tool.c.utils.constants.Actions;
import org.tool.c.utils.constants.Constants;

import java.io.IOException;
import java.time.*;
import java.util.*;

/**
 * Claim for Presence.
 */
public class ClaimPresence {

    /**
     * Get personal time sheet.
     *
     * @param accessToken access token for checkin app
     * @return list time sheet
     * @throws IOException
     */
    public List<TimeSheet> getPersonalTimeSheet(String accessToken) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        String url = bundle.getString("tool.url.checkin");

        RestOperations restOperation = new RestOperations();
        Map<String, Object> data = new HashMap<>();
        data.put("action_name", Actions.PERSONAL_TIMESHEET);

        ResponseEntity<List<TimeSheet>> responseEntity = restOperation.getForListObject(url, HttpMethods.POST, accessToken, TimeSheet.class, data);
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
     * @return true if claim is successfully
     * @throws IOException
     */
    public boolean claim(String accessToken) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        String url = bundle.getString("tool.url.checkin");

        RestOperations restOperation = new RestOperations();
        Map<String, Object> data = new HashMap<>();
        data.put("action_name", Actions.CLAIM_FOR_PRESENCE);

        ResponseEntity<Map> responseEntity = restOperation.getForObject(url, HttpMethods.POST, accessToken, Map.class, data);
        return CommonUtils.getReponseStatus(responseEntity);
    }

    /**
     * Check current time is out of working time.
     *
     * @param timeSheet current timesheet
     * @return true if it's out of working time
     */
    public boolean checkIsOutWorkTime(TimeSheet timeSheet) {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        LocalTime endWorkTime = LocalTime.parse(bundle.getString("working.time.end"));

        return LocalTime.now().isAfter(endWorkTime);
    }
}
