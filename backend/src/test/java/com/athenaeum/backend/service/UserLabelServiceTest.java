package com.athenaeum.backend.service;

import com.athenaeum.backend.dto.SessionLabel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
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
        assertEquals(Arrays.asList(SessionLabel.values()), labels);
    }

    @Test
    void getUserLabels_ForUnknownUser_ShouldReturnEmptyList() {
        UserLabelService service = new UserLabelService();
        
        List<SessionLabel> labels = service.getUserLabels("unknownuser");
        
        assertNotNull(labels);
        assertTrue(labels.isEmpty());
    }
}
