package org.tool.c.subapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.app.action.Authentication;
import org.tool.c.app.action.ClaimPresence;
import org.tool.c.app.entity.TimeSheet;
import org.tool.c.app.entity.Token;
import org.tool.c.app.entity.User;
import org.tool.c.services.email.EmailService;
import org.tool.c.utils.constants.Constants;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Checkin app.
 */
public class CheckinApp {

    private static final Logger LOG = LoggerFactory.getLogger(CheckinApp.class);

    /**
     * Checkin and Checkout.
     */
    public void checkin() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        String username = bundle.getString("tool.user.username");
        String password = bundle.getString("tool.user.password");

        // Get authentication token
        Authentication auth = new Authentication();
        Token authToken = auth.authenticate(username, password);
        String tokenAuth = authToken.getToken();

        // Get Access token
        String urlIdentity = bundle.getString("tool.url.identity");
        Token accessTokenIdentity = auth.getAccessToken(urlIdentity, tokenAuth);
        String tokenAccessIdentity = accessTokenIdentity.getToken();

        // Get oath app
        Map<String, String> oauthAppMap = auth.getOauthApp(tokenAccessIdentity);

        // Get code for oauth checkin app
        String code = auth.getCodeOauthRequest(oauthAppMap, tokenAccessIdentity);

        // Login to checkin app
        User user = auth.loginCheckinApp(oauthAppMap, code);

        // Get Access token for checkin app
        String urlCheckin = bundle.getString("tool.url.checkin");
        Token accessTokenCheckin = auth.getAccessToken(urlCheckin, user.getTokenData().getToken());
        String tokenAccessCheckin = accessTokenCheckin.getToken();

        ClaimPresence claimPresence = new ClaimPresence();

        // Get personal time sheet
        List<TimeSheet> timeSheets = claimPresence.getPersonalTimeSheet(tokenAccessCheckin);
        TimeSheet currentTimeSheet = claimPresence.getCurrentTimeSheet(timeSheets);

        if (null == currentTimeSheet) {
            // Checkin
            boolean success = claimPresence.claim(tokenAccessCheckin);

            if (success) {
                LOG.info("Claim IN successfully");
                EmailService emailService = new EmailService();
                emailService.sendAnnouncement("hung.phamqk@gmail.com", "[INFO] TOOL-C: Check In successfully", "");
            }
        } else {
            boolean isOutWorkingTime = claimPresence.checkIsOutWorkTime(currentTimeSheet);
            if (isOutWorkingTime) {
                long timeSleep = ThreadLocalRandom.current().nextLong(1, 60 * 1000);
                LOG.info("Sleep: " + timeSleep);
//                    Thread.sleep(timeSleep);
                // Checkout
                boolean success = claimPresence.claim(tokenAccessCheckin);

                if (success) {
                    LOG.info("Claim OUT successfully " + LocalTime.now());
                    EmailService emailService = new EmailService();
                    emailService.sendAnnouncement("hung.phamqk@gmail.com", "[INFO] TOOL-C: Check Out successfully", "");
                }
            }
            LOG.info("OUT OF WOKRING TIME: " + isOutWorkingTime);
        }
    }

    /**
     * Run Checkin app.
     */
    public static void run() throws Exception {
        CheckinApp app = new CheckinApp();
        app.checkin();
    }
}
