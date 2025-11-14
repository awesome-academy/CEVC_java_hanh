package com.example.public_service_management.citizen.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.public_service_management.citizen.auth.CitizenDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/citizen/profile")
@RequiredArgsConstructor
public class ProfileController {
  private final ProfileService profileService;

  @GetMapping
  public ResponseEntity<GetProfileDetailsResDto> getDetails(@AuthenticationPrincipal CitizenDetails citizenDetails) {
    GetProfileDetailsResDto profileDetails = profileService.getDetails(citizenDetails.getUser());

    return ResponseEntity.ok().body(profileDetails);
  }
}
