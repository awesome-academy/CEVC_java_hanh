package com.example.public_service_management.citizen.service_type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequiredDocumentResDto {
  private Long id;
  private String note;
  private Boolean isRequired;
  private RequiredDocumentResDto requiredDocument;
}
