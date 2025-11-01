package com.athenaeum.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for HealthCheckController.
 */
@SpringBootTest
@AutoConfigureMockMvc
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthCheck_ShouldReturnUpStatus() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.message").value("Athenaeum Backend is running"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
