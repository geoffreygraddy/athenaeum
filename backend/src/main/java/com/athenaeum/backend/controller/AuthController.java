package com.athenaeum.backend.controller;

import com.athenaeum.backend.dto.AuthResponse;
import com.athenaeum.backend.dto.LoginRequest;
import com.athenaeum.backend.dto.SessionLabel;
import com.athenaeum.backend.dto.UserInfo;
import com.athenaeum.backend.service.UserLabelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Authentication controller for handling login, logout, and user info.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserLabelService userLabelService;

    public AuthController(AuthenticationManager authenticationManager, UserLabelService userLabelService) {
        this.authenticationManager = authenticationManager;
        this.userLabelService = userLabelService;
    }

    /**
     * Login endpoint.
     * 
     * @param loginRequest the login credentials
     * @param request the HTTP request
     * @return authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // Set authentication in SecurityContext
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

            // Store SecurityContext in session
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

            // Get user labels
            List<SessionLabel> labels = userLabelService.getUserLabels(authentication.getName());

            return ResponseEntity.ok(new AuthResponse(true, "Login successful", authentication.getName(), labels));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(false, "Invalid username or password", null));
        }
    }

    /**
     * Logout endpoint.
     * 
     * @param request the HTTP request
     * @return authentication response
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        
        return ResponseEntity.ok(new AuthResponse(true, "Logout successful", null));
    }

    /**
     * Get current user info endpoint.
     * 
     * @return user information
     */
    @GetMapping("/user")
    public ResponseEntity<UserInfo> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() 
            && !authentication.getName().equals("anonymousUser")) {
            List<SessionLabel> labels = userLabelService.getUserLabels(authentication.getName());
            return ResponseEntity.ok(new UserInfo(authentication.getName(), true, labels));
        }
        
        return ResponseEntity.ok(new UserInfo(null, false));
    }
}
