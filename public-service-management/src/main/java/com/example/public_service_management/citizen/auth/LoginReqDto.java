package com.example.public_service_management.citizen.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginReqDto {
  @NotBlank(message = "{validation.email.required}")
  @Email(message = "{validation.email.format}")
  private String email;

  @NotBlank(message = "{validation.password.required}")
  @Size(min = 6, max = 100, message = "{validation.password.length}")
  private String password;
}
