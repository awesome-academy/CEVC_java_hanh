package com.example.public_service_management.admin.service_type;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.public_service_management.category.Category;
import com.example.public_service_management.category.CategoryRepository;
import com.example.public_service_management.common.exceptions.ForbiddenException;
import com.example.public_service_management.common.exceptions.NotFoundException;
import com.example.public_service_management.common.utils.CodeUtil;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.department.Department;
import com.example.public_service_management.department.DepartmentRepository;
import com.example.public_service_management.service_type.ServiceType;
import com.example.public_service_management.service_type.ServiceTypeRepository;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service("adminServiceTypeService")
@RequiredArgsConstructor
public class ServiceTypeService {
  private final ServiceTypeRepository serviceTypeRepository;
  private final DepartmentRepository departmentRepository;
  private final CategoryRepository categoryRepository;
  private final I18nUtil i18nUtil;
  private final ModelMapper modelMapper;

  public Page<GetServiceTypeListDto> getList(String search, Department department, Category category,
      Pageable pageable) {
    Specification<ServiceType> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (search != null && !search.isEmpty()) {
        predicates.add(cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
      }

      if (department != null) {
        predicates.add(cb.equal(root.get("department"), department));
      }

      if (category != null) {
        predicates.add(cb.equal(root.get("category"), category));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    return serviceTypeRepository
        .findAll(spec, pageable)
        .map(serviceType -> modelMapper.map(serviceType, GetServiceTypeListDto.class));
  }

  public ServiceType findServiceType(Long serviceTypeId) {
    return serviceTypeRepository.findById(serviceTypeId)
        .orElseThrow(() -> new NotFoundException(i18nUtil.get("error.service_type.not_found")));
  }

  public GetServiceTypeDetailsDto getDetails(Long serviceTypeId) {
    ServiceType serviceType = findServiceType(serviceTypeId);
    return modelMapper.map(serviceType, GetServiceTypeDetailsDto.class);
  }

  public List<DepartmentDto> getAllDepartments() {
    return departmentRepository.findAll()
        .stream()
        .map(department -> modelMapper.map(department, DepartmentDto.class))
        .toList();
  }

  public List<CategoryDto> getAllCategories() {
    return categoryRepository.findAll()
        .stream()
        .map(category -> modelMapper.map(category, CategoryDto.class))
        .toList();
  }

  public void createServiceType(CreateServiceTypeDto serviceTypeDto) {
    ServiceType serviceType = modelMapper.map(serviceTypeDto, ServiceType.class);
    serviceType.setCode(CodeUtil.genUniqCode("SVT", serviceTypeRepository::existsByCode));

    serviceTypeRepository.save(serviceType);
  }

  public EditServiceTypeDto getServiceTypeToEdit(Long serviceTypeId) {
    ServiceType serviceType = findServiceType(serviceTypeId);
    return modelMapper.map(serviceType, EditServiceTypeDto.class);
  }

  public void updateServiceType(Long serviceTypeId, EditServiceTypeDto serviceTypeDto) {
    ServiceType serviceType = findServiceType(serviceTypeId);
    if (serviceType.isDeleted()) {
      throw new ForbiddenException(i18nUtil.get("error.service_type.deleted"));
    }

    modelMapper.map(serviceTypeDto, serviceType);
    serviceTypeRepository.save(serviceType);
  }

  public void deleteServiceType(Long serviceTypeId) {
    ServiceType serviceType = findServiceType(serviceTypeId);
    serviceType.setDeletedAt(Instant.now());
    serviceTypeRepository.delete(serviceType);
  }
}
