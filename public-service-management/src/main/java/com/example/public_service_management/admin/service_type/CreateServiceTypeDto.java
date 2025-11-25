package com.example.public_service_management.admin.service_type;

import com.example.public_service_management.category.Category;
import com.example.public_service_management.department.Department;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateServiceTypeDto {
  @NotBlank(message = "{validation.name.required}")
  private String name;

  private String description;

  @NotNull(message = "{validation.processing_time.required}")
  @Min(value = 1, message = "{validation.processing_time.min}")
  private Integer processingTime;

  @NotNull(message = "{validation.fee.required}")
  @Min(value = 0, message = "{validation.fee.min}")
  private Double fee;

  @NotNull(message = "{validation.department.required}")
  private Department department;

  @NotNull(message = "{validation.category.required}")
  private Category category;
}
