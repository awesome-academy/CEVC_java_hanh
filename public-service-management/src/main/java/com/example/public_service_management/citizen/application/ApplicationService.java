package com.example.public_service_management.citizen.application;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.public_service_management.application.Application;
import com.example.public_service_management.application.ApplicationRepository;
import com.example.public_service_management.application_attachment.ApplicationAttachment;
import com.example.public_service_management.application_history.ApplicationHistory;
import com.example.public_service_management.common.enums.ApplicationStatus;
import com.example.public_service_management.common.exceptions.BadRequestException;
import com.example.public_service_management.common.exceptions.NotFoundException;
import com.example.public_service_management.common.utils.CodeUtil;
import com.example.public_service_management.common.utils.FileUtil;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.service_type.ServiceType;
import com.example.public_service_management.service_type.ServiceTypeRepository;
import com.example.public_service_management.staff_assignment.StaffAssignmentRepository;
import com.example.public_service_management.user.User;
import com.example.public_service_management.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationService {
  private final ApplicationRepository applicationRepository;
  private final ServiceTypeRepository serviceTypeRepository;
  private final StaffAssignmentRepository staffAssignmentRepository;
  private final UserRepository userRepository;
  private final I18nUtil i18nUtil;
  private final FileUtil fileUtil;

  @Value("${file.max-application-attachments}")
  private int maxAttachments;

  @Transactional
  public void create(User citizen, Long serviceId, List<MultipartFile> attachments) {
    if (attachments != null && attachments.size() > maxAttachments) {
      throw new BadRequestException(i18nUtil.get("error.application_attachments.max", maxAttachments));
    }

    Application app = new Application(
        CodeUtil.genUniqCode("APP", applicationRepository::existsByCode),
        ApplicationStatus.received,
        Instant.now(),
        null,
        citizen,
        findServiceType(serviceId),
        findStaffWithLeastApps(serviceId));

    app.setHistories(buildHistories(app, citizen));

    if (attachments != null) {
      app.setAttachments(buildAttachments(attachments, app, citizen));
    }

    applicationRepository.save(app);
  }

  private ServiceType findServiceType(Long serviceTypeId) {
    return serviceTypeRepository.findById(serviceTypeId)
        .orElseThrow(() -> new NotFoundException(i18nUtil.get("error.service_type.not_found", serviceTypeId)));
  }

  private User findStaffWithLeastApps(Long serviceTypeId) {
    Long staffId = staffAssignmentRepository.countApplicationsByStaff(serviceTypeId).stream()
        .min(Comparator.comparingLong(obj -> (Long) obj[1]))
        .map(obj -> (Long) obj[0])
        .orElseThrow(() -> new NotFoundException(i18nUtil.get("error.no_staff_assigned_for_service_type")));

    return userRepository.findById(staffId)
        .orElseThrow(() -> new NotFoundException(i18nUtil.get("error.user.not_found", staffId)));
  }

  private List<ApplicationAttachment> buildAttachments(List<MultipartFile> attachments, Application app,
      User citizen) {
    return attachments
        .stream()
        .map(file -> {
          return new ApplicationAttachment(
              file.getOriginalFilename(),
              fileUtil.storeFile(file, "application_attachments"),
              file.getContentType(),
              app,
              citizen);
        })
        .collect(Collectors.toList());
  }

  private List<ApplicationHistory> buildHistories(Application app, User citizen) {
    ApplicationHistory history = new ApplicationHistory(
        ApplicationStatus.received,
        null,
        null,
        app,
        citizen);

    return List.of(history);
  }
}
