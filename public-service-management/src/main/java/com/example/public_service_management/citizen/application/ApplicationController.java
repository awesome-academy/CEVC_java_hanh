package com.example.public_service_management.citizen.application;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.public_service_management.citizen.auth.CitizenDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/citizen/applications")
@RequiredArgsConstructor
public class ApplicationController {
  private final ApplicationService applicationService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<CreateApplicationDetailsResDto> create(
      @AuthenticationPrincipal CitizenDetails citizenDetails,
      @RequestParam Long serviceId,
      @RequestParam(required = false) List<MultipartFile> attachments) {

    CreateApplicationDetailsResDto resDto = applicationService.create(citizenDetails.getUser(), serviceId, attachments);
    return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
  }
}
