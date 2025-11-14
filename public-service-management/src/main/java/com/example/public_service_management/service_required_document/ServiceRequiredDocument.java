package com.example.public_service_management.service_required_document;

import org.hibernate.annotations.SQLDelete;

import com.example.public_service_management.common.entity.BaseEntity;
import com.example.public_service_management.required_document.RequiredDocument;
import com.example.public_service_management.service_type.ServiceType;

import jakarta.persistence.Column;
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
@Table(name = "service_required_documents")
@SQLDelete(sql = "UPDATE service_required_documents SET deleted_at = UTC_TIMESTAMP(6) WHERE id = ?")
@Getter
@Setter
public class ServiceRequiredDocument extends BaseEntity {
  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT")
  private String note;

  @Column(nullable = false)
  private Boolean isRequired = true;

  @ManyToOne
  @JoinColumn(name = "service_type_id", nullable = false)
  private ServiceType serviceType;

  @ManyToOne
  @JoinColumn(name = "required_document_id", nullable = false)
  private RequiredDocument requiredDocument;

  public ServiceRequiredDocument() {
  }

  public ServiceRequiredDocument(String note, Boolean isRequired, ServiceType serviceType,
      RequiredDocument requiredDocument) {
    this.note = note;
    this.isRequired = isRequired;
    this.serviceType = serviceType;
    this.requiredDocument = requiredDocument;
  }
}
