package com.example.public_service_management.admin.user;

import com.example.public_service_management.common.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserListDto {
  private Long id;
  private String fullName;
  private String email;
  private Role role;
  private boolean isDeleted;
  private boolean isLocked;

  public String getStatusKey() {
    return isDeleted ? "deleted" : (isLocked ? "locked" : "active");
  }
}
