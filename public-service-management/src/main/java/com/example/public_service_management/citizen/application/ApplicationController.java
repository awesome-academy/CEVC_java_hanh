package com.example.public_service_management.citizen.application;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.public_service_management.common.dto.PageReqDto;
import com.example.public_service_management.common.dto.PageResDto;
import com.example.public_service_management.common.utils.PageableUtil;
import com.example.public_service_management.user.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/citizen/applications")
@RequiredArgsConstructor
public class ApplicationController {
  private final ApplicationService applicationService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<CreateApplicationDetailsResDto> create(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestParam Long serviceId,
      @RequestParam(required = false) List<MultipartFile> attachments) {

    CreateApplicationDetailsResDto resDto = applicationService.create(userDetails.getUser(), serviceId, attachments);
    return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
  }

  @GetMapping
  public ResponseEntity<PageResDto<GetApplicationListResDto>> getList(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Validated @ModelAttribute PageReqDto pageReqDto) {

    PageResDto<GetApplicationListResDto> apps = applicationService.getList(
        userDetails.getUser().getId(),
        PageableUtil.toPageable(pageReqDto));
    return ResponseEntity.ok(apps);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GetApplicationDetailsResDto> getDetails(@PathVariable Long id) {
    GetApplicationDetailsResDto appDetails = applicationService.getDetails(id);
    return ResponseEntity.ok(appDetails);
  }

  @PostMapping(value = "/{id}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> createAttachments(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long id,
      @RequestParam List<MultipartFile> attachments) {

    applicationService.createAttachments(userDetails.getUser(), id, attachments);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
