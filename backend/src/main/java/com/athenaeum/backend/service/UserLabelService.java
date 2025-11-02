package com.athenaeum.backend.service;

import com.athenaeum.backend.dto.SessionLabel;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for managing user session labels.
 * This service determines which labels are assigned to each user.
 */
@Service
public class UserLabelService {

    private final Map<String, List<SessionLabel>> userLabels = new HashMap<>();

    public UserLabelService() {
        // Initialize default labels for the admin user
        // In a real application, this would be stored in a database
        userLabels.put("admin", Arrays.asList(SessionLabel.values()));
    }

    /**
     * Get the labels assigned to a user.
     * 
     * @param username the username
     * @return list of session labels assigned to the user
     */
    public List<SessionLabel> getUserLabels(String username) {
        return userLabels.getOrDefault(username, List.of());
    }
}
