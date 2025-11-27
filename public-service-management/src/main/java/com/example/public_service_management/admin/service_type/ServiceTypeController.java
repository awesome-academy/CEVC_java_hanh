package com.example.public_service_management.admin.service_type;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.public_service_management.category.Category;
import com.example.public_service_management.common.dto.PageReqDto;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.common.utils.PageableUtil;
import com.example.public_service_management.department.Department;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller("adminServiceTypeController")
@RequestMapping("/admin/service-types")
@AllArgsConstructor
public class ServiceTypeController {
  private final ServiceTypeService serviceTypeService;
  private final I18nUtil i18nUtil;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
  }

  @GetMapping
  public String getList(
      Model model,
      @RequestParam(required = false) String search,
      @RequestParam(required = false) Department department,
      @RequestParam(required = false) Category category,
      @Valid @ModelAttribute PageReqDto pageReqDto) {

    Pageable pageable = PageableUtil.toPageable(pageReqDto);
    Page<GetServiceTypeListDto> serviceTypes = serviceTypeService.getList(search, department, category, pageable);

    prepareFormData(model);
    model.addAttribute("serviceTypes", serviceTypes.getContent());
    model.addAttribute("search", search);
    model.addAttribute("department", department);
    model.addAttribute("category", category);
    model.addAttribute("currentPage", serviceTypes.getNumber());
    model.addAttribute("totalPages", serviceTypes.getTotalPages());

    return "admin/service_type/list";
  }

  @GetMapping("/{id}/details")
  public String details(@PathVariable Long id, Model model) {
    GetServiceTypeDetailsDto serviceType = serviceTypeService.getDetails(id);
    model.addAttribute("serviceType", serviceType);

    return "admin/service_type/details";
  }

  @GetMapping("/add")
  public String getAddForm(Model model) {
    prepareFormData(model);
    model.addAttribute("serviceType", new CreateServiceTypeDto());

    return "admin/service_type/add";
  }

  @PostMapping("/add")
  public String add(@Valid @ModelAttribute("serviceType") CreateServiceTypeDto serviceTypeDto,
      BindingResult result,
      RedirectAttributes redirectAttributes,
      Model model) {

    if (result.hasErrors()) {
      prepareFormData(model);
      model.addAttribute("serviceType", serviceTypeDto);

      return "admin/service_type/add";
    }
    serviceTypeService.createServiceType(serviceTypeDto);

    redirectAttributes.addFlashAttribute("success", i18nUtil.get("admin.service_type.created_success"));
    return "redirect:/admin/service-types";
  }

  @GetMapping("/{id}/edit")
  public String getEditForm(@PathVariable Long id, Model model) {
    prepareFormData(model);
    EditServiceTypeDto serviceType = serviceTypeService.getServiceTypeToEdit(id);
    model.addAttribute("serviceType", serviceType);

    return "admin/service_type/edit";
  }

  @PutMapping("/{id}/edit")
  public String edit(@PathVariable Long id,
      @Valid @ModelAttribute("serviceType") EditServiceTypeDto serviceTypeDto,
      BindingResult result,
      RedirectAttributes redirectAttributes,
      Model model) {

    if (result.hasErrors()) {
      prepareFormData(model);
      model.addAttribute("serviceType", serviceTypeDto);
      return "admin/service_type/edit";
    }

    serviceTypeService.updateServiceType(id, serviceTypeDto);
    redirectAttributes.addFlashAttribute("success", i18nUtil.get("admin.service_type.updated_success"));
    return "redirect:/admin/service-types";
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    serviceTypeService.deleteServiceType(id);

    redirectAttributes.addFlashAttribute("success", i18nUtil.get("admin.service_type.deleted_success"));
    return "redirect:/admin/service-types";
  }

  private void prepareFormData(Model model) {
    model.addAttribute("departments", serviceTypeService.getAllDepartments());
    model.addAttribute("categories", serviceTypeService.getAllCategories());
  }
}
