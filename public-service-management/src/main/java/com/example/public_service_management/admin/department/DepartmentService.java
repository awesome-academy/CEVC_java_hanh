package com.example.public_service_management.admin.department;

import java.time.Instant;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.public_service_management.common.enums.Role;
import com.example.public_service_management.common.exceptions.ForbiddenException;
import com.example.public_service_management.common.exceptions.NotFoundException;
import com.example.public_service_management.common.utils.CodeUtil;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.department.Department;
import com.example.public_service_management.department.DepartmentRepository;
import com.example.public_service_management.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {
  private final DepartmentRepository departmentRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final I18nUtil i18nUtil;

  public Page<GetDepartmentListDto> getList(Pageable pageable) {
    return departmentRepository
        .findAll(pageable)
        .map(department -> modelMapper.map(department, GetDepartmentListDto.class));
  }

  public List<UserDto> getAllManager() {
    return userRepository
        .findByRole(Role.manager)
        .stream()
        .map(staff -> modelMapper.map(staff, UserDto.class))
        .toList();
  }

  public GetDepartmentDetailsDto getDetails(Long id) {
    Department department = findDepartment(id);
    return modelMapper.map(department, GetDepartmentDetailsDto.class);
  }

  public void createDepartment(CreateDepartmentDto departmentDto) {
    Department department = modelMapper.map(departmentDto, Department.class);
    department.setCode(CodeUtil.genUniqCode("DPT", departmentRepository::existsByCode));

    departmentRepository.save(department);
  }

  public EditDepartmentDto getDepartmentToEdit(Long id) {
    Department department = findDepartment(id);
    return modelMapper.map(department, EditDepartmentDto.class);
  }

  public void updateDepartment(Long id, EditDepartmentDto departmentDto) {
    Department department = findDepartment(id);

    if (department.isDeleted()) {
      throw new ForbiddenException(i18nUtil.get("error.department.deleted"));
    }

    modelMapper.map(departmentDto, department);

    departmentRepository.save(department);
  }

  public void deleteDepartment(Long id) {
    Department department = findDepartment(id);
    department.setDeletedAt(Instant.now());
    departmentRepository.save(department);
  }

  private Department findDepartment(Long id) {
    return departmentRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(i18nUtil.get("error.department.not_found")));
  }
}
