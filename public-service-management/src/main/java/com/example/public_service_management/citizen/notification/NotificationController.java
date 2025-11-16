package com.example.public_service_management.citizen.notification;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.public_service_management.citizen.auth.CitizenDetails;
import com.example.public_service_management.common.dto.PageReqDto;
import com.example.public_service_management.common.dto.PageResDto;
import com.example.public_service_management.common.utils.PageableUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/citizen/notifications")
@RequiredArgsConstructor
public class NotificationController {
  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<PageResDto<GetNotificationListDto>> getList(
      @AuthenticationPrincipal CitizenDetails citizenDetails,
      @Validated @ModelAttribute PageReqDto pageReqDto) {

    PageResDto<GetNotificationListDto> notifications = notificationService.getList(
        citizenDetails.getUser().getId(),
        PageableUtil.toPageable(pageReqDto));

    return ResponseEntity.ok(notifications);
  }
}
