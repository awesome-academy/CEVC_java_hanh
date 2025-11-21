package com.example.public_service_management.admin.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {
  @NotBlank(message = "{validation.full_name.required}")
  private String fullName;

  private String address;

  @Pattern(regexp = "(\\+84|0)\\d{9}", message = "{validation.phone_number.pattern}")
  private String phoneNumber;

  @NotBlank(message = "{validation.email.required}")
  @Email(message = "{validation.email.format}")
  private String email;

  @NotBlank(message = "{validation.role.required}")
  @Pattern(regexp = "staff|citizen", message = "{validation.role.pattern}")
  private String role;
}
