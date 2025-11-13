package com.example.public_service_management.citizen.auth;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.public_service_management.common.enums.Role;
import com.example.public_service_management.common.exceptions.BadRequestException;
import com.example.public_service_management.common.exceptions.ConflictException;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.user.User;
import com.example.public_service_management.user.UserRepository;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;
  private final I18nUtil i18nUtil;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper,
      I18nUtil i18nUtil) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.modelMapper = modelMapper;
    this.i18nUtil = i18nUtil;
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
}
