package com.example.public_service_management.admin;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.public_service_management.user.CustomUserDetails;
import com.example.public_service_management.user.User;

@Service
public class AdminService {
  public User getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
      throw new RuntimeException("User not authenticated");
    }

    return ((CustomUserDetails) auth.getPrincipal()).getUser();
  }
}
