package com.example.public_service_management.admin.application;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.public_service_management.common.dto.PageReqDto;
import com.example.public_service_management.common.enums.ApplicationStatus;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.common.utils.PageableUtil;
import com.example.public_service_management.service_type.ServiceType;
import com.example.public_service_management.user.User;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;

@Controller("adminApplicationController")
@RequestMapping("/admin/applications")
@AllArgsConstructor
public class ApplicationController {
  private final ApplicationService applicationService;
  private final I18nUtil i18nUtil;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
  }

  @GetMapping
  public String getList(
      Model model,
      @RequestParam(required = false) ServiceType serviceType,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) User citizen,
      @Valid @ModelAttribute PageReqDto pageReqDto) {

    Pageable pageable = PageableUtil.toPageable(pageReqDto);
    Page<GetApplicationListDto> applications = applicationService.getList(serviceType, status, citizen, pageable);

    model.addAttribute("applications", applications.getContent());
    model.addAttribute("statuses", ApplicationStatus.values());
    model.addAttribute("status", status);
    model.addAttribute("serviceTypes", applicationService.getAllServiceTypes());
    model.addAttribute("serviceType", serviceType);
    model.addAttribute("citizens", applicationService.getAllCitizens());
    model.addAttribute("citizen", citizen);
    model.addAttribute("currentPage", applications.getNumber());
    model.addAttribute("totalPages", applications.getTotalPages());

    return "admin/application/list";
  }

  @GetMapping("/{id}/details")
  public String details(@PathVariable Long id, Model model) {
    GetApplicationDetailsDto application = applicationService.getDetails(id);
    model.addAttribute("app", application);

    return "admin/application/details";
  }

  @GetMapping("/{id}/edit")
  public String getEditForm(@PathVariable Long id, Model model) {
    EditApplicationDto application = applicationService.getApplicationToEdit(id);
    model.addAttribute("app", application);
    model.addAttribute("statuses", ApplicationStatus.values());
    model.addAttribute("staffs", applicationService.getAllStaffs());

    return "admin/application/edit";
  }

  @PutMapping("/{id}/edit")
  public String edit(
      @PathVariable Long id,
      @RequestParam ApplicationStatus status,
      @RequestParam(required = false) String note,
      @RequestParam(required = false) MultipartFile attachment,
      @RequestParam(required = false) User staff,
      RedirectAttributes redirectAttributes) {

    applicationService.updateApplication(id, status, note, attachment, staff);

    redirectAttributes.addFlashAttribute("success", i18nUtil.get("admin.application.updated_success"));
    return "redirect:/admin/applications";
  }
}
