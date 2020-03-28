package org.tool.c.app.authen;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("user_id")
    private String userId;
    private String username;
    private String profilePhoto;
    @JsonProperty("user_role")
    private String userRole;
    @JsonProperty("token_data")
    private Token tokenData;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setTokenData(Token tokenData) {
        this.tokenData = tokenData;
    }

    public Token getTokenData() {
        return tokenData;
    }
}
