package com.athenaeum.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService implementation for loading user details.
 * Currently uses in-memory storage with a single user from configuration.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    
    @Value("${spring.security.user.name}")
    private String username;
    
    @Value("${spring.security.user.password}")
    private String password;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals(this.username)) {
            return User.builder()
                .username(this.username)
                .password(passwordEncoder.encode(this.password))
                .roles("USER")
                .build();
        }
        
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
