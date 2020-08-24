package org.tool.c.subapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.app.action.Authentication;
import org.tool.c.app.action.ClaimPresence;
import org.tool.c.app.entity.TimeSheet;
import org.tool.c.app.entity.Token;
import org.tool.c.app.entity.User;
import org.tool.c.base.BaseApp;
import org.tool.c.services.email.EmailService;
import org.tool.c.services.pattern.VelocityService;
import org.tool.c.utils.CommonUtils;
import org.tool.c.utils.constants.Constants;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Checkin app.
 */
public class CheckinApp extends BaseApp {

    private static final Logger LOG = LoggerFactory.getLogger(CheckinApp.class);

    /**
     * Checkin and Checkout.
     */
    public boolean checkin() {
        LOG.info("### START CHECKIN ###");
        LOG.info("Username: " + toolUsername);

        // Get authentication token
        Authentication auth = new Authentication();
        Token authToken = auth.authenticateIdentity(toolUsername, toolPassword);
        String tokenAuth = authToken.getToken();
        LOG.info("Get identity authentication token successful");

        // Get Access token
        Token accessTokenIdentity = auth.getAccessToken(identityUrl, tokenAuth);
        String tokenAccessIdentity = accessTokenIdentity.getToken();
        LOG.info("Get identity access token successful");

        // Get oath app
        Map<String, String> oauthAppMap = auth.getOauthApp(tokenAccessIdentity);
        LOG.info("Get Oauth App map successful");

        // Get code for oauth checkin app
        String code = auth.getCodeOauthRequest(oauthAppMap, tokenAccessIdentity);
        LOG.info("Get Code Oauth successful");

        // Login to checkin app
        User user = auth.loginCheckinApp(oauthAppMap, code);
        LOG.info("Login to checkin app by using Oauth Code successful");

        // Get Access token for checkin app
        Token accessTokenCheckin = auth.getAccessToken(checkinUrl, user.getTokenData().getToken());
        String tokenAccessCheckin = accessTokenCheckin.getToken();
        LOG.info("Get access token for checkin app successfully");

        ClaimPresence claimPresence = new ClaimPresence();

        // Get personal time sheet
        List<TimeSheet> timeSheets = claimPresence.getPersonalTimeSheet(tokenAccessCheckin);
        TimeSheet currentTimeSheet = claimPresence.getCurrentTimeSheet(timeSheets);
        LOG.info("Get list time sheet of user successful");
        int lateTime = claimPresence.calcLateTime(timeSheets);
        LOG.info("Late time: " + lateTime + " minutes");

        VelocityService velocityService = new VelocityService();
        if (null == currentTimeSheet) {
            // Checkin
            TimeSheet timeSheet = claimPresence.claim(tokenAccessCheckin);

            if (null != timeSheet) {
                Map<String, String> dataMail = prepareDataTimeSheet(timeSheet, lateTime);
                LOG.info("Checkin successfully " + dataMail.get("checkInTime"));
                String[] mail = velocityService.mergeForMail("checkin-success", dataMail);
                EmailService emailService = new EmailService();
                emailService.sendAnnouncement(receiveAnnouncement, mail[0], mail[1]);
            }
        } else {
            boolean isOutWorkingTime = claimPresence.checkIsOutWorkTime();
            if (isOutWorkingTime) {
                long timeSleep = ThreadLocalRandom.current().nextLong(1, 60 * 1000);
                LOG.info("Sleep: " + timeSleep);
                CommonUtils.sleep(timeSleep);
                // Checkout
                TimeSheet timeSheet = claimPresence.claim(tokenAccessCheckin);

                if (null != timeSheet) {
                    Map<String, String> dataMail = prepareDataTimeSheet(timeSheet, lateTime);
                    LOG.info("Checkout successfully " + dataMail.get("checkOutTime"));

                    if (Constants.YES.equals(announcementMultiTime)) {
                        String[] mail = velocityService.mergeForMail("checkout-success", dataMail);
                        EmailService emailService = new EmailService();
                        emailService.sendAnnouncement(receiveAnnouncement, mail[0], mail[1]);
                    }
                }
            } else {
                LOG.warn("IN WORKING TIME");
            }
        }

        LOG.info("### END CHECKIN ###");
        return true;
    }

    /**
     * Prepare data time sheet to set mail template.
     *
     * @param timeSheet time sheet
     * @return map data
     */
    public Map<String, String> prepareDataTimeSheet(TimeSheet timeSheet, int lateTime) {
        Map<String, String> map = new HashMap<>();
        map.put("checkInTime", CommonUtils.formatLocalDateTime(CommonUtils.convertToSystemTimeZone(timeSheet.getCheckInTime())));
        map.put("checkOutTime", CommonUtils.formatLocalDateTime(CommonUtils.convertToSystemTimeZone(timeSheet.getCheckOutTime())));
        map.put("workingDay", CommonUtils.formatLocalDate(timeSheet.getWorkDay()));
        map.put("workingHours", calcWorkingHours(timeSheet));
        map.put("lateTime", String.valueOf(lateTime));
        return map;
    }

    /**
     * Calculate working hours.
     *
     * @param timeSheet time sheet
     * @return working hours after minis lunch time
     */
    private String calcWorkingHours(TimeSheet timeSheet) {
        LocalTime endMorningWorkTime = LocalTime.parse(bundle.getString("working.time.morning.end"));
        LocalTime startAfternoonWorkTime = LocalTime.parse(bundle.getString("working.time.afternoon.start"));
        Duration duration = Duration.between(timeSheet.getCheckInTime(), timeSheet.getCheckOutTime());
        long millis = duration.toMillis() - ChronoUnit.MILLIS.between(endMorningWorkTime, startAfternoonWorkTime);

        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return String.format("%02d:%02d:%02d",
                hours,
                minutes - TimeUnit.HOURS.toMinutes(hours),
                seconds - TimeUnit.MINUTES.toSeconds(minutes));
    }

    /**
     * Run Checkin app.
     *
     * @param args arguments
     */
    public static boolean run(String[] args) throws Exception {
        // Print out all arguments
        Arrays.asList(args).forEach(LOG::info);
        CheckinApp app = new CheckinApp();
        return app.checkin();
    }
}
