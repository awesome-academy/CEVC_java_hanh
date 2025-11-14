package com.example.public_service_management.common.utils;

import org.springframework.stereotype.Component;

import com.example.public_service_management.common.exceptions.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenUtil {
  private final I18nUtil i18nUtil;

  public String getAccessToken(HttpServletRequest request) {
    String accessToken = request.getHeader("X-Access-Token");
    if (accessToken == null || accessToken.isBlank()) {
      throw new UnauthorizedException(i18nUtil.get("error.missing_access_token_header"));
    }

    return accessToken;
  }
}
