package com.athenaeum.backend.repository;

import com.athenaeum.backend.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Authority entity.
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    List<Authority> findByUsername(String username);
}
