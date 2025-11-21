package com.example.public_service_management.admin;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.public_service_management.common.exceptions.NotFoundException;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.user.CustomUserDetails;
import com.example.public_service_management.user.User;
import com.example.public_service_management.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {
  private final UserRepository userRepository;
  private final I18nUtil i18nUtil;

  @Override
  public CustomUserDetails loadUserByUsername(String email) throws NotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(i18nUtil.get("error.user.not_found")));

    return new CustomUserDetails(user);
  }
}
