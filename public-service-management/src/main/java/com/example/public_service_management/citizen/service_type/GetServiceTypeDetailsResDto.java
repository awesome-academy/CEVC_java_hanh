package com.example.public_service_management.citizen.service_type;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetServiceTypeDetailsResDto {
  private Long id;
  private String code;
  private String name;
  private String description;
  private Integer processingTime;
  private Double fee;
  private DepartmentResDto department;
  private CategoryResDto category;
  private List<ServiceRequiredDocumentResDto> serviceRequiredDocuments;
}
