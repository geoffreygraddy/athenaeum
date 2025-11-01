package com.athenaeum.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

/**
 * Spring Session configuration to enable JDBC-based session storage.
 */
@Configuration
@EnableJdbcHttpSession
public class SessionConfig {
}
