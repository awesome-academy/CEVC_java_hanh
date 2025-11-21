package com.example.public_service_management.citizen.notification;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.public_service_management.common.dto.PageResDto;
import com.example.public_service_management.notification.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service("citizenNotificationService")
@RequiredArgsConstructor
public class NotificationService {
  private final NotificationRepository notificationRepository;
  private final ModelMapper modelMapper;

  public PageResDto<GetNotificationListDto> getList(Long recipientId, Pageable pageable) {
    Page<GetNotificationListDto> notifications = notificationRepository.findByRecipientId(recipientId, pageable)
        .map(notification -> modelMapper.map(notification, GetNotificationListDto.class));

    return new PageResDto<GetNotificationListDto>(notifications);
  }
}
