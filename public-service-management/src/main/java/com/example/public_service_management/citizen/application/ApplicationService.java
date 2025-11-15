package com.example.public_service_management.citizen.application;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.public_service_management.application.Application;
import com.example.public_service_management.application.ApplicationRepository;
import com.example.public_service_management.application_attachment.ApplicationAttachment;
import com.example.public_service_management.application_attachment.ApplicationAttachmentRepository;
import com.example.public_service_management.application_history.ApplicationHistory;
import com.example.public_service_management.common.dto.PageResDto;
import com.example.public_service_management.common.enums.ApplicationStatus;
import com.example.public_service_management.common.enums.NotificationType;
import com.example.public_service_management.common.exceptions.BadRequestException;
import com.example.public_service_management.common.exceptions.NotFoundException;
import com.example.public_service_management.common.utils.CodeUtil;
import com.example.public_service_management.common.utils.FileUtil;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.notification.NotificationService;
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
  private final ApplicationAttachmentRepository attachmentRepository;
  private final NotificationService notificationService;
  private final I18nUtil i18nUtil;
  private final FileUtil fileUtil;
  private final ModelMapper modelMapper;

  @Value("${file.max-application-attachments}")
  private int maxAttachments;

  @Transactional
  public CreateApplicationDetailsResDto create(User citizen, Long serviceId, List<MultipartFile> attachments) {
    if (attachments != null && attachments.size() > maxAttachments) {
      throw new BadRequestException(i18nUtil.get("error.application_attachments.max", maxAttachments));
    }

    Application app = Application.builder()
        .code(CodeUtil.genUniqCode("APP", applicationRepository::existsByCode))
        .status(ApplicationStatus.received)
        .submittedAt(Instant.now())
        .citizen(citizen)
        .serviceType(findServiceType(serviceId))
        .staff(findStaffWithLeastApps(serviceId))
        .build();

    app.setHistories(buildHistories(app, citizen));

    if (attachments != null) {
      app.setAttachments(buildAttachments(attachments, app, citizen));
    }

    applicationRepository.save(app);

    notificationService.create(NotificationType.application_received, citizen, app);

    return modelMapper.map(app, CreateApplicationDetailsResDto.class);
  }

  public PageResDto<GetApplicationListResDto> getList(Long citizenId, Pageable pageable) {
    Page<GetApplicationListResDto> apps = applicationRepository.findByCitizenId(citizenId, pageable)
        .map(app -> modelMapper.map(app, GetApplicationListResDto.class));

    return new PageResDto<GetApplicationListResDto>(apps);
  }

  public GetApplicationDetailsResDto getDetails(Long appId) {
    Application app = findApplication(appId);

    return modelMapper.map(app, GetApplicationDetailsResDto.class);
  }

  @Transactional
  public void createAttachments(User citizen, Long appId, List<MultipartFile> attachments) {
    Application app = findApplication(appId);

    if ((app.getAttachments().size() + attachments.size()) > maxAttachments) {
      throw new BadRequestException(i18nUtil.get("error.application_attachments.max", maxAttachments));
    }

    buildAttachments(attachments, app, citizen).forEach(attachmentRepository::save);
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

  private List<ApplicationAttachment> buildAttachments(List<MultipartFile> attachments, Application app, User citizen) {
    return attachments
        .stream()
        .map(file -> {
          return ApplicationAttachment.builder()
              .fileName(file.getOriginalFilename())
              .fileUrl(fileUtil.storeFile(file, "application_attachments"))
              .fileType(file.getContentType())
              .application(app)
              .uploadedBy(citizen)
              .build();
        })
        .collect(Collectors.toList());
  }

  private List<ApplicationHistory> buildHistories(Application app, User citizen) {
    ApplicationHistory history = ApplicationHistory.builder()
        .status(ApplicationStatus.received)
        .application(app)
        .updatedBy(citizen)
        .build();

    return List.of(history);
  }

  private Application findApplication(Long appId) {
    return applicationRepository.findById(appId)
        .orElseThrow(() -> new NotFoundException(i18nUtil.get("error.application.not_found", appId)));
  }
}
