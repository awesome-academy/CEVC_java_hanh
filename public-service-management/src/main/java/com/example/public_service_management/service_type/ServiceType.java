package com.example.public_service_management.service_type;

import java.util.List;

import org.hibernate.annotations.SQLDelete;

import com.example.public_service_management.application.Application;
import com.example.public_service_management.category.Category;
import com.example.public_service_management.common.entity.BaseEntity;
import com.example.public_service_management.department.Department;
import com.example.public_service_management.service_required_document.ServiceRequiredDocument;
import com.example.public_service_management.staff_assignment.StaffAssignment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service_types")
@SQLDelete(sql = "UPDATE service_types SET deleted_at = UTC_TIMESTAMP(6) WHERE id = ?")
@Getter
@Setter
public class ServiceType extends BaseEntity {
  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String code;

  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  private Integer processingTime;

  private Double fee;

  @ManyToOne
  @JoinColumn(name = "department_id", nullable = false)
  private Department department;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @OneToMany(mappedBy = "serviceType", fetch = FetchType.LAZY)
  private List<ServiceRequiredDocument> serviceRequiredDocuments;

  @OneToMany(mappedBy = "serviceType", fetch = FetchType.LAZY)
  private List<Application> applications;

  @OneToMany(mappedBy = "serviceType", fetch = FetchType.LAZY)
  private List<StaffAssignment> staffAssignments;

  public ServiceType() {
  }

  public ServiceType(String code, String name, String description, Integer processingTime, Double fee,
      Department department, Category category) {
    this.code = code;
    this.name = name;
    this.description = description;
    this.processingTime = processingTime;
    this.fee = fee;
    this.department = department;
    this.category = category;
  }
}
