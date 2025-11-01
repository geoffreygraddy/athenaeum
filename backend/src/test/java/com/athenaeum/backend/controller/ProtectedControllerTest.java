package com.athenaeum.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for ProtectedController.
 * 
 * Note: Uses @WithMockUser to simulate authenticated users in unit tests.
 * Full end-to-end session-based authentication requires integration testing.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProtectedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void protectedResource_WithoutAuthentication_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/protected/resource"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void protectedResource_WithAuthentication_ShouldReturnResource() throws Exception {
        mockMvc.perform(get("/api/protected/resource")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a protected resource"))
                .andExpect(jsonPath("$.user").value("admin"))
                .andExpect(jsonPath("$.authorities").isArray());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void protectedResource_WithDifferentUser_ShouldReturnResourceForThatUser() throws Exception {
        mockMvc.perform(get("/api/protected/resource")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a protected resource"))
                .andExpect(jsonPath("$.user").value("testuser"))
                .andExpect(jsonPath("$.authorities").isArray());
    }
}
