package com.example.public_service_management.admin.department;

import com.example.public_service_management.user.User;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditDepartmentDto {
  private Long id;

  @NotBlank(message = "{validation.name.required}")
  private String name;

  private String address;

  private User leader;
}
