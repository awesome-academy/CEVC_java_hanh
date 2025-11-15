package com.example.public_service_management.notification;

import org.springframework.stereotype.Service;

import com.example.public_service_management.application.Application;
import com.example.public_service_management.common.enums.NotificationType;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.common.utils.MailUtil;
import com.example.public_service_management.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
  private final NotificationRepository notificationRepository;
  private final I18nUtil i18nUtil;
  private final MailUtil mailUtil;

  public void create(NotificationType type, User recipient, Application app) {
    String title = i18nUtil.get("notification.title." + type.name());
    String content = i18nUtil.get("notification.content." + type.name());
    String metadata = "{\"applicationId\": " + app.getId() + "}";

    Notification notification = Notification.builder()
        .title(title)
        .content(content)
        .type(type)
        .recipient(recipient)
        .metadata(metadata)
        .build();

    if (recipient.getEmailNotificationEnabled()) {
      mailUtil.sendSimpleMailAsync(recipient.getEmail(), title, content);

      notification.setEmailSent(true);
    }

    notificationRepository.save(notification);
  }
}
