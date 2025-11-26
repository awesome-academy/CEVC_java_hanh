package com.example.public_service_management.admin.user;

import java.time.LocalDate;

import com.example.public_service_management.common.enums.Gender;
import com.example.public_service_management.common.enums.Role;
import com.example.public_service_management.common.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserDetailsDto {
  private Long id;
  private String fullName;
  private LocalDate dateOfBirth;
  private Gender gender;
  private String nationalId;
  private String address;
  private String phoneNumber;
  private String email;
  private Role role = Role.citizen;
  private DepartmentDto department;
  private String eidIdentifier;
  private UserStatus status = UserStatus.active;
  private Boolean emailNotificationEnabled = true;
  private boolean isDeleted;
  private boolean isLocked;

  public String getStatusKey() {
    return isDeleted ? "deleted" : (isLocked ? "locked" : "active");
  }
}
