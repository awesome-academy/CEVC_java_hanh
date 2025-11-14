package com.example.public_service_management.citizen.service_type;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.public_service_management.common.dto.PageResDto;
import com.example.public_service_management.service_type.ServiceTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceTypeService {
  private final ServiceTypeRepository serviceTypeRepository;
  private final ModelMapper modelMapper;

  public PageResDto<GetServiceTypeListResDto> getList(Long categoryId, Pageable pageable) {
    Page<GetServiceTypeListResDto> serviceTypes = serviceTypeRepository.findByCategoryId(categoryId, pageable)
        .map(st -> modelMapper.map(st, GetServiceTypeListResDto.class));

    return new PageResDto<GetServiceTypeListResDto>(serviceTypes);
  }
}
