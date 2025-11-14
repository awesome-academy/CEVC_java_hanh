package com.example.public_service_management.citizen.service_type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequiredDocumentResDto {
  private Long id;
  private String code;
  private String name;
  private String description;
  private String sampleFileUrl;
  private String fileType;
}
