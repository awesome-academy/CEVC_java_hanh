package com.example.public_service_management.required_document;

import java.util.List;

import org.hibernate.annotations.SQLDelete;

import com.example.public_service_management.service_required_document.ServiceRequiredDocument;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "required_documents")
@SQLDelete(sql = "UPDATE required_documents SET deleted_at = UTC_TIMESTAMP(6) WHERE id = ?")
@Getter
@Setter
public class RequiredDocument {
  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String code;

  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  private String sampleFileUrl;

  private String fileType;

  @OneToMany(mappedBy = "requiredDocument", fetch = FetchType.LAZY)
  private List<ServiceRequiredDocument> serviceRequiredDocuments;

  public RequiredDocument() {
  }

  public RequiredDocument(String code, String name, String description, String sampleFileUrl, String fileType) {
    this.code = code;
    this.name = name;
    this.description = description;
    this.sampleFileUrl = sampleFileUrl;
    this.fileType = fileType;
  }
}
