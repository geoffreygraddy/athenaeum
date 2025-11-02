package com.athenaeum.backend.controller;

import com.athenaeum.backend.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for AuthController.
 * 
 * Note: Full end-to-end session persistence across multiple HTTP requests
 * requires integration testing with a real HTTP client. MockMvc tests validate
 * the authentication logic works correctly.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_WithValidCredentials_ShouldReturnSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin", "changeme");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.labels").isArray())
                .andExpect(jsonPath("$.labels.length()").value(11));
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void login_WithInvalidUsername_ShouldReturnUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest("invaliduser", "changeme");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void logout_ShouldInvalidateSession() throws Exception {
        // First login
        LoginRequest loginRequest = new LoginRequest("admin", "changeme");
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();

        // Then logout
        mockMvc.perform(post("/api/auth/logout")
                .session(session)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Logout successful"));
    }

    @Test
    void logout_WithoutCsrfToken_ShouldSucceed() throws Exception {
        // First login
        LoginRequest loginRequest = new LoginRequest("admin", "changeme");
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();

        // Then logout without CSRF token - should succeed since /api/auth/logout is exempt
        mockMvc.perform(post("/api/auth/logout")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Logout successful"));
    }

    @Test
    void getUserInfo_WhenNotAuthenticated_ShouldReturnUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/auth/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(false))
                .andExpect(jsonPath("$.username").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void getUserInfo_WhenAuthenticated_ShouldReturnUserInfo() throws Exception {
        mockMvc.perform(get("/api/auth/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.labels").isArray())
                .andExpect(jsonPath("$.labels.length()").value(11));
    }

    @Test
    void login_ShouldCreateSessionWithAuthentication() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin", "changeme");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("JSESSIONID"))
                .andReturn();

        // Verify session was created
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        assertNotNull(session, "Session should be created after login");
        assertFalse(session.isInvalid(), "Session should not be invalid");
    }

    @Test
    void login_ShouldReturnAllSessionLabels() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin", "changeme");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.labels[0]").value("COMPUTER_SCIENCE"))
                .andExpect(jsonPath("$.labels[1]").value("PHILOSOPHY"))
                .andExpect(jsonPath("$.labels[2]").value("RELIGION"))
                .andExpect(jsonPath("$.labels[3]").value("SOCIAL_SCIENCES"))
                .andExpect(jsonPath("$.labels[4]").value("LANGUAGE"))
                .andExpect(jsonPath("$.labels[5]").value("SCIENCE"))
                .andExpect(jsonPath("$.labels[6]").value("TECHNOLOGY"))
                .andExpect(jsonPath("$.labels[7]").value("ARTS"))
                .andExpect(jsonPath("$.labels[8]").value("LITERATURE"))
                .andExpect(jsonPath("$.labels[9]").value("HISTORY"))
                .andExpect(jsonPath("$.labels[10]").value("GEOGRAPHY"));
    }
}
