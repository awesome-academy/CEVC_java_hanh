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
public class GetApplicationListResDto {
  private Long id;
  private String code;
  private String status;
  private Instant submittedAt;
  private Instant completedAt;
  private ServiceTypeResDto serviceType;
}
