package com.example.public_service_management.citizen.auth;

import java.time.LocalDate;

import com.example.public_service_management.common.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterReqDto {
  @NotBlank(message = "{validation.national_id.required}")
  @Size(min = 9, max = 12, message = "{validation.national_id.length}")
  private String nationalId;

  @NotBlank(message = "{validation.full_name.required}")
  private String fullName;

  @Past(message = "{validation.date_of_birth.past}")
  private LocalDate dateOfBirth;

  private Gender gender;

  private String address;

  @Pattern(regexp = "(\\+84|0)\\d{9}", message = "{validation.phone_number.pattern}")
  private String phoneNumber;

  @NotBlank(message = "{validation.email.required}")
  @Email(message = "{validation.email.format}")
  private String email;

  @NotBlank(message = "{validation.password.required}")
  @Size(min = 6, max = 100, message = "{validation.password.length}")
  private String password;

  @NotBlank(message = "{validation.password_confirmation.required}")
  private String passwordConfirmation;
}
