package org.tool.c.utils.constants;

/**
 * Action Constants class.
 */
public class Actions {

    /**
     * Private Constructor to prevent new ActionConstants class.
     */
    private Actions() {
    }

    /**
     * Identity app.
     */
    public static final String GET_OAUTH_TOKEN = "get_oauth_token";
    public static final String GET_ACCESS_TOKEN = "get_access_token";
    public static final String GET_OAUTH_APP = "get_oauth_app";
    public static final String CREATE_OAUTH_REQUEST = "create_oauth_request";

    /**
     * Checkin app.
     */
    public static final String LOGIN = "login";
    public static final String PERSONAL_TIMESHEET = "personal_timesheet";
    public static final String CLAIM_FOR_PRESENCE = "claim_for_presence";
}
