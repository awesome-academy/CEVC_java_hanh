package com.example.public_service_management.notification;

import org.hibernate.annotations.SQLDelete;

import com.example.public_service_management.common.entity.BaseEntity;
import com.example.public_service_management.common.enums.NotificationType;
import com.example.public_service_management.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@SQLDelete(sql = "UPDATE notifications SET deleted_at = UTC_TIMESTAMP(6) WHERE id = ?")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {
  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isRead = false;

  @Column(nullable = false)
  @Builder.Default
  private Boolean emailSent = false;

  @Column(columnDefinition = "json")
  private String metadata;

  @ManyToOne
  @JoinColumn(name = "recipient_id", nullable = false)
  private User recipient;
}
