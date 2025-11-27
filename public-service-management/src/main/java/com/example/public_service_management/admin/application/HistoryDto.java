package com.example.public_service_management.admin.application;

import java.time.Instant;

import com.example.public_service_management.common.enums.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDto {
  private Long id;
  private ApplicationStatus status;
  private String note;
  private String attachmentUrl;
  private Instant createdAt;
  private UserDto updatedBy;

  public String getStatusName() {
    return status != null ? status.name() : null;
  }
}
