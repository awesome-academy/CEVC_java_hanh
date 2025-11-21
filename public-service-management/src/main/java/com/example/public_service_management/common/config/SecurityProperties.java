package com.example.public_service_management.common.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {
  private List<SecurityRule> rules;
}
