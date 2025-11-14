package com.example.public_service_management.citizen.service_type;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.public_service_management.common.dto.PageResDto;
import com.example.public_service_management.common.exceptions.NotFoundException;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.service_type.ServiceType;
import com.example.public_service_management.service_type.ServiceTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceTypeService {
  private final ServiceTypeRepository serviceTypeRepository;
  private final ModelMapper modelMapper;
  private final I18nUtil i18nUtil;

  public PageResDto<GetServiceTypeListResDto> getList(Long categoryId, Pageable pageable) {
    Page<GetServiceTypeListResDto> serviceTypes = serviceTypeRepository.findByCategoryId(categoryId, pageable)
        .map(st -> modelMapper.map(st, GetServiceTypeListResDto.class));

    return new PageResDto<GetServiceTypeListResDto>(serviceTypes);
  }

  public GetServiceTypeDetailsResDto getDetails(Long serviceTypeId) {
    ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
        .orElseThrow(() -> new NotFoundException(i18nUtil.get("error.service_type.not_found", serviceTypeId)));

    return modelMapper.map(serviceType, GetServiceTypeDetailsResDto.class);
  }
}
