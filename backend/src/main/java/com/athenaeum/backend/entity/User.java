package com.athenaeum.backend.entity;

import jakarta.persistence.*;

/**
 * Entity class for users table.
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(length = 45)
    private String username;
    
    @Column(length = 60)
    private String password;
    
    @Column(nullable = false)
    private Integer enabled;
    
    // Constructors
    public User() {
    }
    
    public User(String username, String password, Integer enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Integer getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
}
