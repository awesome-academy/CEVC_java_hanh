package com.example.public_service_management.application_attachment;

import org.hibernate.annotations.SQLDelete;

import com.example.public_service_management.application.Application;
import com.example.public_service_management.common.entity.BaseEntity;
import com.example.public_service_management.user.User;

import jakarta.persistence.Entity;
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
@Table(name = "application_attachments")
@SQLDelete(sql = "UPDATE application_attachments SET deleted_at = UTC_TIMESTAMP(6) WHERE id = ?")
@Getter
@Setter
public class ApplicationAttachment extends BaseEntity {
  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String fileName;

  private String fileUrl;

  private String fileType;

  @ManyToOne
  @JoinColumn(name = "application_id", nullable = false)
  private Application application;

  @ManyToOne
  @JoinColumn(name = "uploaded_by_id", nullable = false)
  private User uploadedBy;

  public ApplicationAttachment() {
  }

  public ApplicationAttachment(String fileName, String fileUrl, String fileType, Application application,
      User uploadedBy) {
    this.fileName = fileName;
    this.fileUrl = fileUrl;
    this.fileType = fileType;
    this.application = application;
    this.uploadedBy = uploadedBy;
  }
}
