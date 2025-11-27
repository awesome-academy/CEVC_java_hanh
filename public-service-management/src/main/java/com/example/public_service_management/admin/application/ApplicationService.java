package com.example.public_service_management.admin.application;

import java.time.Instant;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.public_service_management.admin.AdminService;
import com.example.public_service_management.application.Application;
import com.example.public_service_management.application.ApplicationRepository;
import com.example.public_service_management.application_history.ApplicationHistoriesRepository;
import com.example.public_service_management.application_history.ApplicationHistory;
import com.example.public_service_management.common.enums.ApplicationStatus;
import com.example.public_service_management.common.enums.NotificationType;
import com.example.public_service_management.common.enums.Role;
import com.example.public_service_management.common.exceptions.NotFoundException;
import com.example.public_service_management.common.utils.FileUtil;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.notification.NotificationService;
import com.example.public_service_management.service_type.ServiceType;
import com.example.public_service_management.service_type.ServiceTypeRepository;
import com.example.public_service_management.user.User;
import com.example.public_service_management.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service("adminApplicationService")
@RequiredArgsConstructor
public class ApplicationService {
  private final ApplicationRepository applicationRepository;
  private final ServiceTypeRepository serviceTypeRepository;
  private final UserRepository userRepository;
  private final ApplicationHistoriesRepository applicationHistoriesRepository;
  private final AdminService adminService;
  private final NotificationService notificationService;
  private final ModelMapper modelMapper;
  private final I18nUtil i18nUtil;
  private final FileUtil fileUtil;

  public Page<GetApplicationListDto> getList(ServiceType serviceType, String status, User citizen, Pageable pageable) {
    User user = adminService.getCurrentUser();
    Specification<Application> spec = ApplicationSpecification.buildForList(user, serviceType, status, citizen);

    return applicationRepository
        .findAll(spec, pageable)
        .map(app -> modelMapper.map(app, GetApplicationListDto.class));
  }

  public List<ServiceTypeDto> getAllServiceTypes() {
    return serviceTypeRepository
        .findAll()
        .stream()
        .map(st -> modelMapper.map(st, ServiceTypeDto.class))
        .toList();
  }

  public List<UserDto> getAllCitizens() {
    return userRepository
        .findByRole(Role.citizen)
        .stream()
        .map(citizen -> modelMapper.map(citizen, UserDto.class))
        .toList();
  }

  public Application findApplication(Long id) {
    User user = adminService.getCurrentUser();
    Specification<Application> spec = ApplicationSpecification.buildForDetail(user, id);

    return applicationRepository.findOne(spec)
        .orElseThrow(() -> new NotFoundException(i18nUtil.get("error.application.not_found")));
  }

  public GetApplicationDetailsDto getDetails(Long id) {
    Application application = findApplication(id);
    return modelMapper.map(application, GetApplicationDetailsDto.class);
  }

  public EditApplicationDto getApplicationToEdit(Long id) {
    Application application = findApplication(id);
    return modelMapper.map(application, EditApplicationDto.class);
  }

  @Transactional
  public void updateApplication(Long id, ApplicationStatus status, String note, MultipartFile attachment, User staff) {
    Application application = findApplication(id);

    boolean hasAttachment = attachment != null && !attachment.isEmpty();
    boolean hasNote = note != null && !note.isBlank();
    if (application.getStatus() == status && application.getStaff() == staff && !hasAttachment && !hasNote) {
      return;
    }

    application.setStatus(status);
    application.setStaff(staff);
    if (status == ApplicationStatus.approved || status == ApplicationStatus.rejected) {
      application.setCompletedAt(Instant.now());
    }
    applicationRepository.save(application);

    createHistory(application, status, note, attachment);
    createNotification(application, status, hasAttachment, hasNote);
  }

  public List<UserDto> getAllStaffs() {
    return userRepository
        .findByRole(Role.staff)
        .stream()
        .map(staff -> modelMapper.map(staff, UserDto.class))
        .toList();
  }

  private void createHistory(Application app, ApplicationStatus status, String note, MultipartFile attachment) {
    User user = adminService.getCurrentUser();
    String attachmentUrl = attachment != null && !attachment.isEmpty()
        ? fileUtil.storeFile(attachment, "application_histories")
        : null;

    ApplicationHistory history = ApplicationHistory.builder()
        .status(status)
        .note(note)
        .attachmentUrl(attachmentUrl)
        .application(app)
        .updatedBy(user)
        .build();

    applicationHistoriesRepository.save(history);
  }

  private void createNotification(Application app, ApplicationStatus status, boolean hasAttachment, boolean hasNote) {
    if (status == ApplicationStatus.processing && (hasAttachment || hasNote)) {
      notificationService.create(NotificationType.request_supplement, app.getCitizen(), app);
    } else if (status == ApplicationStatus.approved || status == ApplicationStatus.rejected) {
      notificationService.create(NotificationType.application_result, app.getCitizen(), app);
    }
  }
}
