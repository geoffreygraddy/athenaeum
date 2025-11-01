package com.athenaeum.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Athenaeum Backend.
 * Currently configured to permit all requests for initial development.
 * This should be properly configured with authentication/authorization in production.
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
            .csrf(csrf -> csrf.disable()); // Disable CSRF for API endpoints (configure properly in production)
        
        return http.build();
    }
}
