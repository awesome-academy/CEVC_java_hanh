package com.example.public_service_management.citizen.profile;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.public_service_management.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
  private final ModelMapper modelMapper;

  public GetProfileDetailsResDto getDetails(User user) {
    return modelMapper.map(user, GetProfileDetailsResDto.class);
  }
}
