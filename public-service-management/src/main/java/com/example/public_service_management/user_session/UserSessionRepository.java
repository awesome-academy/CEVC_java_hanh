package com.example.public_service_management.user_session;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
  Optional<UserSession> findBySessionId(String sessionId);

  @Transactional
  @Modifying
  @Query("DELETE FROM UserSession us WHERE us.sessionId = :sessionId")
  void hardDeleteBySessionId(@Param("sessionId") String sessionId);
}
