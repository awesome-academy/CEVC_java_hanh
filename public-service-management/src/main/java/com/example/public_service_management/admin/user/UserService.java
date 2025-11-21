package com.example.public_service_management.admin.user;

import java.time.Instant;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.public_service_management.common.enums.UserStatus;
import com.example.public_service_management.common.exceptions.ConflictException;
import com.example.public_service_management.common.exceptions.ForbiddenException;
import com.example.public_service_management.common.exceptions.NotFoundException;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.department.DepartmentRepository;
import com.example.public_service_management.user.User;
import com.example.public_service_management.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final DepartmentRepository departmentRepository;
  private final I18nUtil i18nUtil;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;

  public Page<GetUserListDto> getList(List<String> roles, String role, Pageable pageable) {
    List<String> filteredRoles = roles;
    if (role != null && !role.isEmpty() && roles.contains(role.toLowerCase())) {
      filteredRoles = List.of(role.toLowerCase());
    }

    Page<GetUserListDto> users = userRepository.findByRoles(filteredRoles, pageable)
        .map(u -> modelMapper.map(u, GetUserListDto.class));

    return users;
  }

  public User findUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(i18nUtil.get("error.user.not_found", userId)));
  }

  public GetUserDetailsDto getDetails(Long id) {
    User user = findUser(id);
    return modelMapper.map(user, GetUserDetailsDto.class);
  }

  public EditUserDto getUserToEdit(Long id) {
    User user = findUser(id);
    return modelMapper.map(user, EditUserDto.class);
  }

  public List<DepartmentDto> getAllDepartments() {
    return departmentRepository.findAll().stream()
        .map(dept -> modelMapper.map(dept, DepartmentDto.class))
        .toList();
  }

  public void updateUser(Long id, EditUserDto userDto) {
    if (userRepository.existsByEmailAndIdNot(userDto.getEmail(), id)) {
      throw new ConflictException(i18nUtil.get("error.email.exists"));
    }
    if (userRepository.existsByNationalIdAndIdNot(userDto.getNationalId(), id)) {
      throw new ConflictException(i18nUtil.get("error.national_id.exists"));
    }
    if (userDto.getEidIdentifier() != null && !userDto.getEidIdentifier().isBlank()
        && userRepository.existsByEidIdentifierAndIdNot(userDto.getEidIdentifier(), id)) {
      throw new ConflictException(i18nUtil.get("error.eid_identifier.exists"));
    }

    User user = findUser(id);
    if (user.isDeleted()) {
      throw new ForbiddenException(i18nUtil.get("error.account_deleted"));
    }
    if (user.isLocked()) {
      throw new ForbiddenException(i18nUtil.get("error.account_locked"));
    }

    modelMapper.map(userDto, user);
    userRepository.save(user);
  }

  public void createUser(CreateUserDto userDto) {
    if (userRepository.existsByEmail(userDto.getEmail())) {
      throw new ConflictException(i18nUtil.get("error.email.exists"));
    }

    User user = modelMapper.map(userDto, User.class);
    String encodedPassword = passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(8));
    user.setPasswordDigest(encodedPassword);

    userRepository.save(user);
  }

  public void deleteUser(Long id) {
    User user = findUser(id);
    user.setDeletedAt(Instant.now());
    userRepository.save(user);
  }

  public void changeStatus(Long id, UserStatus status) {
    User user = findUser(id);
    if (user.isDeleted()) {
      throw new ForbiddenException(i18nUtil.get("error.account_deleted"));
    }

    user.setStatus(status);
    userRepository.save(user);
  }
}
