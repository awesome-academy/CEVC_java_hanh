package com.example.public_service_management.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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
@EnableConfigurationProperties(SecurityProperties.class)
@RequiredArgsConstructor
public class SecurityConfig {
  private final TokenAuthFilter tokenAuthFilter;

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Order(1)
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .securityMatcher("/api/**")
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/citizen/auth/register").permitAll()
            .requestMatchers("/api/citizen/auth/login").permitAll()
            .requestMatchers("/api/citizen/auth/logout").permitAll()
            .requestMatchers("/api/citizen/**").hasRole("CITIZEN")
            .anyRequest().authenticated())
        .addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  @Order(2)
  SecurityFilterChain webFilterChain(HttpSecurity http, SecurityProperties props) throws Exception {
    http.securityMatcher("/admin/**")
        .authorizeHttpRequests(auth -> {
          props.getRules().forEach(rule -> {
            var matcher = rule.getMethod() == null
                ? auth.requestMatchers(rule.getPattern())
                : auth.requestMatchers(HttpMethod.valueOf(rule.getMethod()), rule.getPattern());

            if (rule.getRoles() == null) {
              matcher.permitAll();
            } else {
              matcher.hasAnyRole(rule.getRoles());
            }
          });
          auth.anyRequest().authenticated();
        })
        .formLogin(form -> form
            .loginPage("/admin/login")
            .loginProcessingUrl("/admin/login")
            .failureUrl("/admin/login?error=true")
            .defaultSuccessUrl("/admin/dashboard", true)
            .permitAll())
        .logout(logout -> logout
            .logoutUrl("/admin/logout")
            .logoutSuccessUrl("/admin/login?logout=true")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll());

    return http.build();
  }
}
