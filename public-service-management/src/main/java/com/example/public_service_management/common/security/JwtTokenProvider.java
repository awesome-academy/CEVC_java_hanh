package com.example.public_service_management.common.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenProvider {
  @Value("${jwt.secret}")
  private String jwtSecret;

  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public String generateToken(String sessionID, long expiresInSeconds) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expiresInSeconds * 1000);

    return Jwts.builder()
        .setSubject(sessionID)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public String getSessionId(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}
