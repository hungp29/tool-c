package org.tool.c;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.app.authen.Authentication;
import org.tool.c.app.authen.Token;
import org.tool.c.app.claim.ClaimPresence;
import org.tool.c.utils.constants.Constants;

import java.io.IOException;
import java.util.ResourceBundle;

public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        String username = bundle.getString("tool.user.id");
        String password = bundle.getString("tool.user.password");

        try {
            // Get authentication token
            Authentication auth = new Authentication();
            Token authToken = auth.authenticate(username, password);
            String tokenAuth = authToken.getToken();

            // Get Access token
            Token accessToken = auth.getAccessToken(tokenAuth);
            String tokenAccess = accessToken.getToken();

            // Claim for Presence
            ClaimPresence claimPresence = new ClaimPresence();
            claimPresence.claim(tokenAccess);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
