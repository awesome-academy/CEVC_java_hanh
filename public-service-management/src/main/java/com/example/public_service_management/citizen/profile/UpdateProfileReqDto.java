package com.example.public_service_management.citizen.profile;

import java.time.LocalDate;

import com.example.public_service_management.common.enums.Gender;
import com.example.public_service_management.common.validation.OptionalNotBlank;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileReqDto {
  @OptionalNotBlank
  private String fullName;

  @Past(message = "{validation.date_of_birth.past}")
  private LocalDate dateOfBirth;

  private Gender gender;

  private String address;

  @Pattern(regexp = "(\\+84|0)\\d{9}", message = "{validation.phone_number.pattern}")
  private String phoneNumber;

  @OptionalNotBlank
  @Email(message = "{validation.email.format}")
  private String email;

  private Boolean emailNotificationEnabled;
}
