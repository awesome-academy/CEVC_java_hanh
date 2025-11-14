package com.example.public_service_management.category;

import java.util.List;

import org.hibernate.annotations.SQLDelete;

import com.example.public_service_management.common.entity.BaseEntity;
import com.example.public_service_management.service_type.ServiceType;

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
@Table(name = "categories")
@SQLDelete(sql = "UPDATE categories SET deleted_at = UTC_TIMESTAMP(6) WHERE id = ?")
@Getter
@Setter
public class Category extends BaseEntity {
  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String code;

  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<ServiceType> serviceTypes;

  public Category() {
  }

  public Category(String code, String name, String description) {
    this.code = code;
    this.name = name;
    this.description = description;
  }
}
