package com.wheremoney.demo.repository;

import com.wheremoney.demo.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query(value = "SELECT COUNT(s) FROM sessions s WHERE user_id= ?1 AND logged_in=false", nativeQuery = true)
    int failedSignInAttempts(Long userId);

    @Query(value = "SELECT * FROM sessions WHERE user_id= ?1 ORDER BY created_date DESC LIMIT 1", nativeQuery = true)
    Optional<Session> lastUserSession(Long userId);
}
