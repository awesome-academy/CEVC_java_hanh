package com.example.public_service_management.citizen.service_type;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.public_service_management.common.dto.PageReqDto;
import com.example.public_service_management.common.dto.PageResDto;
import com.example.public_service_management.common.utils.PageableUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/citizen/service-types")
@RequiredArgsConstructor
public class ServiceTypeController {
  private final ServiceTypeService serviceTypeService;

  @GetMapping
  public ResponseEntity<PageResDto<GetServiceTypeListResDto>> getList(
      @RequestParam(required = false) Long categoryId,
      @Validated @ModelAttribute PageReqDto pageReqDto) {

    Pageable pageable = PageableUtil.toPageable(pageReqDto);
    PageResDto<GetServiceTypeListResDto> serviceTypes = serviceTypeService.getList(categoryId, pageable);

    return ResponseEntity.ok(serviceTypes);
  }
}
