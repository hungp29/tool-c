package org.tool.c.subapp;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.app.action.AccessTokenAction;
import org.tool.c.app.action.Authentication;
import org.tool.c.app.action.AuthenticationTokenAction;
import org.tool.c.app.action.ClaimPresence;
import org.tool.c.app.action.OAuthAction;
import org.tool.c.app.entity.TimeSheet;
import org.tool.c.app.entity.Token;
import org.tool.c.app.entity.User;
import org.tool.c.bundle.AppBundle;
import org.tool.c.services.email.EmailService;
import org.tool.c.services.pattern.VelocityService;
import org.tool.c.utils.CommonUtils;
import org.tool.c.utils.JarFileUtils;
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
public class CheckinApp extends AppBundle {

    private static final Logger LOG = LoggerFactory.getLogger(CheckinApp.class);

    private static final String MAIL_TYPE_CHECKIN = "checkin-success";
    private static final String MAIL_TYPE_CHECKOUT = "checkout-success";

    private ClaimPresence claimPresence;
    private String version;

    /**
     * Default Constructor.
     */
    public CheckinApp(String version) {
        this.version = version;

        claimPresence = new ClaimPresence();
    }

    /**
     * Checkin and Checkout.
     */
    public boolean checkin(boolean runAnything) {
        LOG.info("### START CHECKIN ###");
        LOG.info("Username: " + toolUsername);
        Authentication auth = new Authentication();

        // Get authentication token
        AuthenticationTokenAction authenticationTokenAction = new AuthenticationTokenAction();
        Token authToken = authenticationTokenAction.authenticateIdentity(toolUsername, toolPassword);
        String tokenAuth = authToken.getToken();
        LOG.info("Get identity authentication token successful");

        // Get Access token
        AccessTokenAction accessTokenAction = new AccessTokenAction();
        Token accessTokenIdentity = accessTokenAction.getAccessToken(identityUrl, tokenAuth);
        String tokenAccessIdentity = accessTokenIdentity.getToken();
        LOG.info("Get identity access token successful");

        // Get oath app
        OAuthAction oAuthAction = new OAuthAction();
        Map<String, String> oauthAppMap = oAuthAction.getOauthApp(tokenAccessIdentity);
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

        // Get personal time sheet
        List<TimeSheet> timeSheets = claimPresence.getPersonalTimeSheet(tokenAccessCheckin);
        TimeSheet currentTimeSheet = claimPresence.getCurrentTimeSheet(timeSheets);
        LOG.info("Get list time sheet of user successful");

        if (null == currentTimeSheet) {
            // Checkin
            TimeSheet timeSheet = claimPresence.claim(tokenAccessCheckin);

            sendMail(MAIL_TYPE_CHECKIN, timeSheets, timeSheet);
        } else {
            boolean isOutWorkingTime = claimPresence.checkIsOutWorkTime();
            if (isOutWorkingTime || runAnything) {
                if (!runAnything && !claimPresence.isFirstCheckout(timeSheets)) {
                    long timeSleep = ThreadLocalRandom.current().nextLong(1, 60 * 1000);
                    LOG.info("Sleep: " + timeSleep);
                    CommonUtils.sleep(timeSleep);
                }
                // Checkout
                TimeSheet timeSheet = claimPresence.claim(tokenAccessCheckin);

                sendMail(MAIL_TYPE_CHECKOUT, timeSheets, timeSheet);
            } else {
                LOG.warn("IN WORKING TIME");
            }
        }

        LOG.info("### END CHECKIN ###");
        return true;
    }

    /**
     * Send mail checkin/checkout successfully to user.
     *
     * @param type       mail template (checkin/checkout)
     * @param timeSheets list timesheet of user
     * @param timeSheet  checkin/checkout timesheet
     */
    private void sendMail(String type, List<TimeSheet> timeSheets, TimeSheet timeSheet) {
        String mailTemplate = MAIL_TYPE_CHECKIN;
        if (MAIL_TYPE_CHECKOUT.equals(type)) {
            mailTemplate = MAIL_TYPE_CHECKOUT;
        }

        if (null != timeSheet) {
            if (!timeSheets.contains(timeSheet)) {
                timeSheets.add(timeSheet);
            } else {
                timeSheets.set(timeSheets.indexOf(timeSheet), timeSheet);
            }
            int lateTime = claimPresence.calcLateTime(timeSheets);
            LOG.info("Latencies Time: " + lateTime + " minutes");

            Map<String, String> dataMail = prepareDataTimeSheet(timeSheet, lateTime);
            if (MAIL_TYPE_CHECKIN.equals(type)) {
                LOG.info("Checkin successfully " + dataMail.get("checkInTime"));
            } else {
                LOG.info("Checkout successfully " + dataMail.get("checkOutTime"));
            }

            if (Constants.YES.equals(announcementMultiTime)) {
                VelocityService velocityService = new VelocityService();
                String[] mail = velocityService.mergeForMail(mailTemplate, dataMail);
                EmailService emailService = new EmailService();
                emailService.sendAnnouncement(receiveAnnouncement, mail[0], mail[1]);
            }
        }
    }

    /**
     * Prepare data time sheet to set mail template.
     *
     * @param timeSheet time sheet
     * @return map data
     */
    public Map<String, String> prepareDataTimeSheet(TimeSheet timeSheet, int lateTime) {
        Map<String, String> map = new HashMap<>();
        map.put("checkInTime", CommonUtils.formatLocalDateTime(CommonUtils.convertToSystemTimeZone(timeSheet.getCheckInTime()), Constants.TIME_FM));
        map.put("checkOutTime", CommonUtils.formatLocalDateTime(CommonUtils.convertToSystemTimeZone(timeSheet.getCheckOutTime()), Constants.TIME_FM));
        map.put("workingDay", CommonUtils.formatLocalDate(timeSheet.getWorkDay()));
        map.put("workingHours", calcWorkingHours(timeSheet));
        map.put("lateTime", String.valueOf(lateTime));
        if (!StringUtils.isEmpty(version)) {
            map.put("version", version);
        }
        map.put("isLate", String.valueOf(claimPresence.isLate(timeSheet)));
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

        long millis = Duration.between(timeSheet.getCheckInTime(), timeSheet.getCheckOutTime()).toMillis();

        if (!CommonUtils.convertToSystemTimeZone(timeSheet.getCheckOutTime()).toLocalTime().isBefore(startAfternoonWorkTime)) {
            millis -= ChronoUnit.MILLIS.between(endMorningWorkTime, startAfternoonWorkTime);
        }

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

        String version = JarFileUtils.getVersion();
        if (null != version && !version.isEmpty()) {
            LOG.info("VERSION: " + version);
        }

        boolean runAnything = false;
        if (args.length > 0) {
            // Print out all arguments
            LOG.info("Arguments");
            Arrays.asList(args).forEach(LOG::info);
            runAnything = Boolean.parseBoolean(args[0]);
        }

        CheckinApp app = new CheckinApp(version);
        return app.checkin(runAnything);
    }
}
