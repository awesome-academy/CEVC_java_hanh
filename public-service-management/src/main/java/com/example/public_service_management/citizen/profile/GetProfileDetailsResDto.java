package com.example.public_service_management.citizen.profile;

import java.time.LocalDate;

import com.example.public_service_management.common.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProfileDetailsResDto {
  private Long id;
  private String fullName;
  private LocalDate dateOfBirth;
  private Gender gender;
  private String nationalId;
  private String address;
  private String phoneNumber;
  private String email;
  private String eidIdentifier;
  private Boolean emailNotificationEnabled;
}
