package com.example.public_service_management.application;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.SQLDelete;

import com.example.public_service_management.application_attachment.ApplicationAttachment;
import com.example.public_service_management.application_history.ApplicationHistory;
import com.example.public_service_management.common.entity.BaseEntity;
import com.example.public_service_management.common.enums.ApplicationStatus;
import com.example.public_service_management.service_type.ServiceType;
import com.example.public_service_management.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "applications")
@SQLDelete(sql = "UPDATE applications SET deleted_at = UTC_TIMESTAMP(6) WHERE id = ?")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application extends BaseEntity {
  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private ApplicationStatus status = ApplicationStatus.received;

  private Instant submittedAt;

  private Instant completedAt;

  @ManyToOne
  @JoinColumn(name = "citizen_id", nullable = false)
  private User citizen;

  @ManyToOne
  @JoinColumn(name = "service_type_id", nullable = false)
  private ServiceType serviceType;

  @ManyToOne
  @JoinColumn(name = "staff_id", nullable = false)
  private User staff;

  @OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ApplicationAttachment> attachments;

  @OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ApplicationHistory> histories;
}
