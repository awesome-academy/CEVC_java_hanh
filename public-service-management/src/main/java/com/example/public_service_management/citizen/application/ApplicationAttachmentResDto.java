package com.example.public_service_management.citizen.application;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationAttachmentResDto {
  private Long id;
  private String fileName;
  private String fileUrl;
  private String fileType;
  private Instant createdAt;
}
