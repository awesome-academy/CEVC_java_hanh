package com.example.public_service_management.user_session;

import org.hibernate.annotations.SQLDelete;

import com.example.public_service_management.common.entity.BaseEntity;
import com.example.public_service_management.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_sessions", indexes = {
    @Index(name = "idx_session_id", columnList = "sessionId")
})
@SQLDelete(sql = "UPDATE user_sessions SET deleted_at = UTC_TIMESTAMP(6) WHERE id = ?")
@Getter
@Setter
public class UserSession extends BaseEntity {
  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String sessionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public UserSession() {
  }

  public UserSession(String sessionId, User user) {
    this.sessionId = sessionId;
    this.user = user;
  }
}
