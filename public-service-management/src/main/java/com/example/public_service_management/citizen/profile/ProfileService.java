package com.example.public_service_management.citizen.profile;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.public_service_management.common.exceptions.ConflictException;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.user.User;
import com.example.public_service_management.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
  private final ModelMapper modelMapper;
  private final UserRepository userRepository;
  private final I18nUtil i18nUtil;

  public GetProfileDetailsResDto getDetails(User user) {
    return modelMapper.map(user, GetProfileDetailsResDto.class);
  }

  public void update(User user, UpdateProfileReqDto reqDto) {
    if (userRepository.existsByEmailAndIdNot(reqDto.getEmail(), user.getId())) {
      throw new ConflictException(i18nUtil.get("error.email.exists"));
    }

    modelMapper.map(reqDto, user);
    userRepository.save(user);
  }
}
