package com.athenaeum.backend.service;

import com.athenaeum.backend.dto.SessionLabel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserLabelService.
 */
class UserLabelServiceTest {

    @Test
    void getUserLabels_ForAdminUser_ShouldReturnAllLabels() {
        UserLabelService service = new UserLabelService();
        
        List<SessionLabel> labels = service.getUserLabels("admin");
        
        assertNotNull(labels);
        assertEquals(11, labels.size());
        assertTrue(labels.contains(SessionLabel.COMPUTER_SCIENCE));
        assertTrue(labels.contains(SessionLabel.PHILOSOPHY));
        assertTrue(labels.contains(SessionLabel.RELIGION));
        assertTrue(labels.contains(SessionLabel.SOCIAL_SCIENCES));
        assertTrue(labels.contains(SessionLabel.LANGUAGE));
        assertTrue(labels.contains(SessionLabel.SCIENCE));
        assertTrue(labels.contains(SessionLabel.TECHNOLOGY));
        assertTrue(labels.contains(SessionLabel.ARTS));
        assertTrue(labels.contains(SessionLabel.LITERATURE));
        assertTrue(labels.contains(SessionLabel.HISTORY));
        assertTrue(labels.contains(SessionLabel.GEOGRAPHY));
    }

    @Test
    void getUserLabels_ForUnknownUser_ShouldReturnEmptyList() {
        UserLabelService service = new UserLabelService();
        
        List<SessionLabel> labels = service.getUserLabels("unknownuser");
        
        assertNotNull(labels);
        assertTrue(labels.isEmpty());
    }
}
