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
public class EditApplicationDto {
  private Long id;
  private ApplicationStatus status;
  private UserDto staff;
}
