package com.athenaeum.backend.dto;

/**
 * Enum representing the labels that can be assigned to a user's session.
 * These labels determine which library documents a user can view.
 */
public enum SessionLabel {
    COMPUTER_SCIENCE("Computer Science"),
    PHILOSOPHY("Philosophy"),
    RELIGION("Religion"),
    SOCIAL_SCIENCES("Social Sciences"),
    LANGUAGE("Language"),
    SCIENCE("Science"),
    TECHNOLOGY("Technology"),
    ARTS("Arts"),
    LITERATURE("Literature"),
    HISTORY("History"),
    GEOGRAPHY("Geography");

    private final String displayName;

    SessionLabel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
