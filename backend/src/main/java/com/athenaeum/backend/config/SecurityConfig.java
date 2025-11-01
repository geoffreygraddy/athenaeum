package com.athenaeum.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Athenaeum Backend.
 * Currently configured to permit all requests for initial development.
 * 
 * PRODUCTION CONSIDERATIONS:
 * - Enable CSRF protection for stateful endpoints
 * - Implement proper authentication (JWT, OAuth2, etc.)
 * - Configure CORS appropriately for frontend integration
 * - Use HTTPS in production environments
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/**", "/actuator/health").permitAll()
                .anyRequest().authenticated()
            )
            // CSRF is disabled for initial development with stateless REST APIs
            // TODO: Enable CSRF protection when implementing session-based authentication
            // or configure exemptions only for token-based API endpoints
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
}
