package org.tool.c.base;

import org.tool.c.utils.constants.Constants;

import java.io.Serializable;
import java.util.ResourceBundle;

public class Base implements Serializable {

    protected ResourceBundle bundle;
    protected String identityUrl;
    protected String checkinUrl;
    protected String algorithm;
    protected String velocityResourceLoader;
    protected String velocityClassResourceLoader;
    protected String velocityTemplateMail;

    public Base() {
        bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        identityUrl = bundle.getString("tool.url.identity");
        checkinUrl = bundle.getString("tool.url.checkin");
        algorithm = bundle.getString("crypto.algorithm");
        velocityResourceLoader = bundle.getString("velocity.resource.loader");
        velocityClassResourceLoader = bundle.getString("velocity.class.resource.loader.class");
        velocityTemplateMail = bundle.getString("velocity.template.mail");
    }
}
