package com.athenaeum.backend.config;

import com.athenaeum.backend.entity.Authority;
import com.athenaeum.backend.entity.User;
import com.athenaeum.backend.repository.AuthorityRepository;
import com.athenaeum.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initializes the database with default users and authorities.
 * Only runs when using H2 database (default profile).
 */
@Component
@ConditionalOnProperty(name = "spring.jpa.database-platform", havingValue = "org.hibernate.dialect.H2Dialect")
public class DataInitializer {
    
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    
    public DataInitializer(UserRepository userRepository, AuthorityRepository authorityRepository, 
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostConstruct
    public void init() {
        // Check if data already exists
        if (userRepository.count() == 0) {
            // Insert initial user for tests (admin/changeme)
            User adminUser = new User("admin", passwordEncoder.encode("changeme"), 1);
            userRepository.save(adminUser);
            
            // Insert initial authority for admin
            Authority adminAuthority = new Authority("admin", "USER");
            authorityRepository.save(adminAuthority);
            
            // Insert geoffrey user as specified in requirements
            User geoffreyUser = new User("geoffrey", passwordEncoder.encode("12345"), 1);
            userRepository.save(geoffreyUser);
            
            // Insert initial authority for geoffrey
            Authority geoffreyAuthority = new Authority("geoffrey", "write");
            authorityRepository.save(geoffreyAuthority);
        }
    }
}
