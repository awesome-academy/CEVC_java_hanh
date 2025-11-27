package com.example.public_service_management.admin.application;

import com.example.public_service_management.common.enums.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetApplicationListDto {
  private Long id;
  private String code;
  private ApplicationStatus status;
  private UserDto citizen;
  private ServiceTypeDto serviceType;

  public String getStatusName() {
    return status.name();
  }
}
