package com.example.public_service_management.admin.service_type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetServiceTypeDetailsDto {
  private Long id;
  private String code;
  private String name;
  private String description;
  private Integer processingTime;
  private Double fee;
  private DepartmentDto department;
  private CategoryDto category;
  private boolean isDeleted;

  public String getStatusKey() {
    return isDeleted ? "deleted" : "active";
  }
}
