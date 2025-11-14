package com.example.public_service_management.citizen.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.public_service_management.common.utils.TokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/citizen/auth")
public class AuthController {
  private final AuthService authService;
  private final TokenUtil tokenUtil;

  public AuthController(AuthService authService, TokenUtil tokenUtil) {
    this.authService = authService;
    this.tokenUtil = tokenUtil;
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(@Valid @RequestBody RegisterReqDto reqDto) {
    authService.register(reqDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResDto> login(@Valid @RequestBody LoginReqDto reqDto) {
    LoginResDto resDto = authService.login(reqDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
  }

  @DeleteMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request) {
    String accessToken = tokenUtil.getAccessToken(request);
    authService.logout(accessToken);
    return ResponseEntity.noContent().build();
  }
}
