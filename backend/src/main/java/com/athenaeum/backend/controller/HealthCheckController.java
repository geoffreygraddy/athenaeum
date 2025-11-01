package com.athenaeum.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller for the Athenaeum Backend API.
 */
@RestController
@RequestMapping("/api")
public class HealthCheckController {

    /**
     * Simple health check endpoint.
     * 
     * @return JSON response with status and timestamp
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Athenaeum Backend is running");
        response.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }
}
