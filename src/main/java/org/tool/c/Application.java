package org.tool.c;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.app.authen.Authentication;
import org.tool.c.app.authen.Token;
import org.tool.c.app.authen.User;
import org.tool.c.app.claim.ClaimPresence;
import org.tool.c.utils.constants.Constants;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        String username = bundle.getString("tool.user.username");
        String password = bundle.getString("tool.user.password");

        try {
            // Get authentication token
            Authentication auth = new Authentication();
            Token authToken = auth.authenticate(username, password);
            String tokenAuth = authToken.getToken();

            // Get Access token
            String urlIdentity = bundle.getString("tool.url.identity");
            Token accessTokenIdentity = auth.getAccessToken(urlIdentity, tokenAuth);
            String tokenAccessIdentity = accessTokenIdentity.getToken();
            System.out.println("Identity: " + tokenAccessIdentity);

            // Get oath app
            Map<String, ?> oauthAppMap = auth.getOauthApp(tokenAccessIdentity);

            // Get code for oauth checkin app
            String code = auth.getCodeOauthRequest(oauthAppMap, tokenAccessIdentity);

            // Login to checkin app
            User user = auth.loginCheckinApp(oauthAppMap, code);

            // Get Access token for checkin app
            String urlCheckin = bundle.getString("tool.url.checkin");
            Token accessTokenCheckin = auth.getAccessToken(urlCheckin, user.getTokenData().getToken());
            String tokenAccessCheckin = accessTokenCheckin.getToken();
            System.out.println("Checkin: " + tokenAccessCheckin);

            // Claim for Presence
            ClaimPresence claimPresence = new ClaimPresence();
            claimPresence.claim(tokenAccessCheckin);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
