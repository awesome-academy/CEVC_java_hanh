package com.example.public_service_management.citizen.auth;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.public_service_management.common.enums.Role;
import com.example.public_service_management.common.exceptions.BadRequestException;
import com.example.public_service_management.common.exceptions.ConflictException;
import com.example.public_service_management.common.exceptions.ForbiddenException;
import com.example.public_service_management.common.exceptions.UnauthorizedException;
import com.example.public_service_management.common.security.JwtTokenProvider;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.user.User;
import com.example.public_service_management.user.UserRepository;
import com.example.public_service_management.user_session.UserSession;
import com.example.public_service_management.user_session.UserSessionRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;
  private final I18nUtil i18nUtil;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserSessionRepository userSessionRepository;

  @Value("${jwt.access-token-expires-in}")
  private long accessTokenExpiresIn;

  @Value("${jwt.refresh-token-expires-in}")
  private long refreshTokenExpiresIn;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper,
      I18nUtil i18nUtil, JwtTokenProvider jwtTokenProvider, UserSessionRepository userSessionRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.modelMapper = modelMapper;
    this.i18nUtil = i18nUtil;
    this.jwtTokenProvider = jwtTokenProvider;
    this.userSessionRepository = userSessionRepository;
  }

  public void register(RegisterReqDto reqDto) {
    if (!reqDto.getPassword().equals(reqDto.getPasswordConfirmation())) {
      throw new BadRequestException(i18nUtil.get("error.password_confirmation.mismatch"));
    }
    if (userRepository.existsByEmail(reqDto.getEmail())) {
      throw new ConflictException(i18nUtil.get("error.email.exists"));
    }
    if (userRepository.existsByNationalId(reqDto.getNationalId())) {
      throw new ConflictException(i18nUtil.get("error.national_id.exists"));
    }

    User user = modelMapper.map(reqDto, User.class);

    String encodedPassword = passwordEncoder.encode(reqDto.getPassword());
    user.setPasswordDigest(encodedPassword);
    user.setRole(Role.citizen);
    userRepository.save(user);
  }

  public LoginResDto login(LoginReqDto reqDto) {
    User user = userRepository.findByEmail(reqDto.getEmail())
        .orElseThrow(() -> new UnauthorizedException(i18nUtil.get("error.invalid_email_or_password")));

    if (!passwordEncoder.matches(reqDto.getPassword(), user.getPasswordDigest())) {
      throw new UnauthorizedException(i18nUtil.get("error.invalid_email_or_password"));
    }

    if (user.isLocked()) {
      throw new ForbiddenException(i18nUtil.get("error.account_locked"));
    }

    if (user.isDeleted()) {
      throw new ForbiddenException(i18nUtil.get("error.account_deleted"));
    }

    String sessionId = UUID.randomUUID().toString();
    String accessToken = jwtTokenProvider.generateToken(sessionId, accessTokenExpiresIn);
    String refreshToken = jwtTokenProvider.generateToken(sessionId, refreshTokenExpiresIn);

    UserSession userSession = new UserSession(sessionId, user);
    userSessionRepository.save(userSession);

    return new LoginResDto(accessToken, refreshToken, accessTokenExpiresIn, refreshTokenExpiresIn);
  }

  public void logout(String accessToken) {
    String sessionId;
    try {
      sessionId = jwtTokenProvider.getSessionId(accessToken);
    } catch (ExpiredJwtException e) {
      sessionId = e.getClaims().getSubject();
    } catch (JwtException e) {
      throw new UnauthorizedException(i18nUtil.get("error.invalid_access_token"));
    }

    userSessionRepository.hardDeleteBySessionId(sessionId);
  }
}
