package com.example.public_service_management.admin.application;

import java.time.Instant;
import java.util.List;

import com.example.public_service_management.common.enums.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetApplicationDetailsDto {
  private Long id;
  private String code;
  private ApplicationStatus status;
  private Instant submittedAt;
  private Instant completedAt;
  private UserDto citizen;
  private UserDto staff;
  private ServiceTypeDto serviceType;
  private List<AttachmentDto> attachments;
  private List<HistoryDto> histories;

  public String getStatusName() {
    return status.name();
  }
}
