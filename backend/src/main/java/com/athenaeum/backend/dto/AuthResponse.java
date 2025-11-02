package com.athenaeum.backend.dto;

import java.util.List;

/**
 * DTO for authentication response.
 */
public class AuthResponse {
    private boolean success;
    private String message;
    private String username;
    private List<SessionLabel> labels;

    public AuthResponse() {
    }

    public AuthResponse(boolean success, String message, String username) {
        this.success = success;
        this.message = message;
        this.username = username;
    }

    public AuthResponse(boolean success, String message, String username, List<SessionLabel> labels) {
        this.success = success;
        this.message = message;
        this.username = username;
        this.labels = labels;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<SessionLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<SessionLabel> labels) {
        this.labels = labels;
    }
}
