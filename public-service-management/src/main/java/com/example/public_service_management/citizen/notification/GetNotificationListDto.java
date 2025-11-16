package com.example.public_service_management.citizen.notification;

import com.example.public_service_management.common.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetNotificationListDto {
  private Long id;
  private String title;
  private String content;
  private NotificationType type;
  private boolean isRead;
  private String metadata;
  private String createdAt;
}
