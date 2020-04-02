package org.tool.c.base;

import org.tool.c.utils.constants.Constants;

import java.io.Serializable;
import java.util.ResourceBundle;

public class BaseApp implements Serializable {

    protected ResourceBundle bundle;
    protected String toolUsername;
    protected String toolPassword;
    protected String identityUrl;
    protected String checkinUrl;
    protected String receiveAnnouncement;

    public BaseApp() {
        bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        toolUsername = bundle.getString("tool.user.username");
        toolPassword = bundle.getString("tool.user.password");
        identityUrl = bundle.getString("tool.url.identity");
        checkinUrl = bundle.getString("tool.url.checkin");
        receiveAnnouncement = bundle.getString("email.announcement.receive");
    }
}
