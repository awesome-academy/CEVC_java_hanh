package com.example.public_service_management.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.public_service_management.common.security.TokenAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final TokenAuthFilter tokenAuthFilter;

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/citizen/auth/register").permitAll()
            .requestMatchers("/api/citizen/auth/login").permitAll()
            .requestMatchers("/api/citizen/auth/logout").permitAll()
            .requestMatchers("/api/citizen/**").hasRole("CITIZEN")
            .anyRequest().authenticated())
        .addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
