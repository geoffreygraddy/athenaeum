package com.athenaeum.backend.dto;

import java.util.List;

/**
 * DTO for user information.
 */
public class UserInfo {
    private String username;
    private boolean authenticated;
    private List<SessionLabel> labels;

    public UserInfo() {
    }

    public UserInfo(String username, boolean authenticated) {
        this.username = username;
        this.authenticated = authenticated;
        this.labels = List.of(); // Initialize to empty list for consistency
    }

    public UserInfo(String username, boolean authenticated, List<SessionLabel> labels) {
        this.username = username;
        this.authenticated = authenticated;
        this.labels = labels;
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

    public List<SessionLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<SessionLabel> labels) {
        this.labels = labels;
    }
}
