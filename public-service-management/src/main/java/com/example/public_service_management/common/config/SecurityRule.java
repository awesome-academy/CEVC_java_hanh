package com.example.public_service_management.common.config;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class SecurityRule {
  private String pattern;
  private String method;
  private String[] roles;
}
