package com.athenaeum.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Protected resource controller to demonstrate authentication requirement.
 */
@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    /**
     * Protected endpoint that requires authentication.
     * 
     * @return JSON response with user information
     */
    @GetMapping("/resource")
    public ResponseEntity<Map<String, Object>> getProtectedResource() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a protected resource");
        response.put("user", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        
        return ResponseEntity.ok(response);
    }
}
