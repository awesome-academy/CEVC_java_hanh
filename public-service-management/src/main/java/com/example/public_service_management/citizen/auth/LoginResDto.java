package com.example.public_service_management.citizen.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResDto {
  private String accessToken;
  private String refreshToken;
  private long accessTokenExpiresIn;
  private long refreshTokenExpiresIn;
}
