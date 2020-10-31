package org.tool.c.bundle;

import org.tool.c.utils.constants.Constants;

import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * Application Bundle to load application properties.
 *
 * @author hungp
 */
public class AppBundle implements Serializable {

    protected ResourceBundle bundle;
    protected String identityUrl;
    protected String checkinUrl;
    protected String algorithm;
    protected String velocityResourceLoader;
    protected String velocityClassResourceLoader;
    protected String velocityTemplateMail;
    protected String toolUsername;
    protected String toolPassword;
    protected String receiveAnnouncement;
    protected String announcementMultiTime;

    public AppBundle() {
        bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        identityUrl = bundle.getString("tool.url.identity");
        checkinUrl = bundle.getString("tool.url.checkin");
        algorithm = bundle.getString("crypto.algorithm");
        velocityResourceLoader = bundle.getString("velocity.resource.loader");
        velocityClassResourceLoader = bundle.getString("velocity.class.resource.loader.class");
        velocityTemplateMail = bundle.getString("velocity.template.mail");
        toolUsername = bundle.getString("tool.user.username");
        toolPassword = bundle.getString("tool.user.password");
        receiveAnnouncement = bundle.getString("email.announcement.receive");
        announcementMultiTime = bundle.getString("tool.user.announcement.multi-time");
    }
}
