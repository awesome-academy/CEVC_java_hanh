package com.example.public_service_management.citizen.auth;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.public_service_management.common.exceptions.UnauthorizedException;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.user_session.UserSession;
import com.example.public_service_management.user_session.UserSessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CitizenDetailsService implements UserDetailsService {
  private final UserSessionRepository userSessionRepository;
  private final I18nUtil i18nUtil;

  @Override
  public CitizenDetails loadUserByUsername(String sessionId) throws UnauthorizedException {
    UserSession userSession = userSessionRepository.findBySessionId(sessionId)
        .orElseThrow(() -> new UnauthorizedException(i18nUtil.get("error.invalid_access_token")));

    return new CitizenDetails(userSession.getUser());
  }
}
