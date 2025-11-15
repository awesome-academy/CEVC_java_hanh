package com.example.public_service_management.application_history;

import org.hibernate.annotations.SQLDelete;

import com.example.public_service_management.application.Application;
import com.example.public_service_management.common.entity.BaseEntity;
import com.example.public_service_management.common.enums.ApplicationStatus;
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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "application_histories")
@SQLDelete(sql = "UPDATE application_histories SET deleted_at = UTC_TIMESTAMP(6) WHERE id = ?")
@Getter
@Setter
public class ApplicationHistory extends BaseEntity {
  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ApplicationStatus status;

  @Column(columnDefinition = "TEXT")

  private String note;

  private String attachmentUrl;

  @ManyToOne
  @JoinColumn(name = "application_id", nullable = false)
  private Application application;

  @ManyToOne
  @JoinColumn(name = "updated_by_id", nullable = false)
  private User updatedBy;

  public ApplicationHistory() {
  }

  public ApplicationHistory(ApplicationStatus status, String note, String attachmentUrl, Application application,
      User updatedBy) {
    this.status = status;
    this.note = note;
    this.attachmentUrl = attachmentUrl;
    this.application = application;
    this.updatedBy = updatedBy;
  }
}
