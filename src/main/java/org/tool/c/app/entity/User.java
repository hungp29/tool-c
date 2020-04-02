package org.tool.c.app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(username, user.username) &&
                Objects.equals(profilePhoto, user.profilePhoto) &&
                Objects.equals(userRole, user.userRole) &&
                Objects.equals(tokenData, user.tokenData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, profilePhoto, userRole, tokenData);
    }
}
