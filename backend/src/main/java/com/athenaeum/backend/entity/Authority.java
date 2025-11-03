package com.athenaeum.backend.entity;

import jakarta.persistence.*;

/**
 * Entity class for authorities table.
 */
@Entity
@Table(name = "authorities")
public class Authority {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(length = 45)
    private String username;
    
    @Column(length = 45)
    private String authority;
    
    // Constructors
    public Authority() {
    }
    
    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
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
    
    public String getAuthority() {
        return authority;
    }
    
    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
