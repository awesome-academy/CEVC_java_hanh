package com.example.public_service_management.admin.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetDepartmentListDto {
  private Long id;
  private String code;
  private String name;
  private boolean isDeleted;

  public String getStatusKey() {
    return isDeleted ? "deleted" : "active";
  }
}
