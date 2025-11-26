package com.example.public_service_management.admin.user;

import java.time.LocalDate;

import com.example.public_service_management.common.enums.Gender;
import com.example.public_service_management.department.Department;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDto {
  private Long id;

  @NotBlank(message = "{validation.full_name.required}")
  private String fullName;

  @Past(message = "{validation.date_of_birth.past}")
  private LocalDate dateOfBirth;

  private Gender gender;

  @NotBlank(message = "{validation.national_id.required}")
  @Size(min = 9, max = 12, message = "{validation.national_id.length}")
  private String nationalId;

  private String address;

  @Pattern(regexp = "(\\+84|0)\\d{9}", message = "{validation.phone_number.pattern}")
  private String phoneNumber;

  @NotBlank(message = "{validation.email.required}")
  @Email(message = "{validation.email.format}")
  private String email;

  @NotBlank(message = "{validation.role.required}")
  @Pattern(regexp = "staff|citizen", message = "{validation.role.pattern}")
  private String role;

  private Department department;

  private String eidIdentifier;

  private Boolean emailNotificationEnabled;
}
