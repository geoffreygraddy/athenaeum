package com.athenaeum.backend.dto;

/**
 * DTO for user information.
 */
public class UserInfo {
    private String username;
    private boolean authenticated;

    public UserInfo() {
    }

    public UserInfo(String username, boolean authenticated) {
        this.username = username;
        this.authenticated = authenticated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
