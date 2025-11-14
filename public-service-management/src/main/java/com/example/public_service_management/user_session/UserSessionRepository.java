package com.example.public_service_management.user_session;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
  Optional<UserSession> findBySessionId(String sessionId);
}
