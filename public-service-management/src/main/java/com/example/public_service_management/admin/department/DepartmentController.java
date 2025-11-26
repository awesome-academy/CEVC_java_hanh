package com.example.public_service_management.admin.department;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.public_service_management.common.dto.PageReqDto;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.common.utils.PageableUtil;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/admin/departments")
@AllArgsConstructor
public class DepartmentController {
  private final DepartmentService departmentService;
  private final I18nUtil i18nUtil;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
  }

  @GetMapping
  public String getList(Model model, @Valid @ModelAttribute PageReqDto pageReqDto) {
    Pageable pageable = PageableUtil.toPageable(pageReqDto);
    Page<GetDepartmentListDto> departmentDtos = departmentService.getList(pageable);

    prepareFormData(model);
    model.addAttribute("departments", departmentDtos);
    model.addAttribute("currentPage", departmentDtos.getNumber());
    model.addAttribute("totalPages", departmentDtos.getTotalPages());

    return "admin/department/list";
  }

  @GetMapping("/{id}/details")
  public String details(@PathVariable Long id, Model model) {
    GetDepartmentDetailsDto departmentDto = departmentService.getDetails(id);

    model.addAttribute("department", departmentDto);

    return "admin/department/details";
  }

  @GetMapping("/add")
  public String getAddForm(Model model) {
    prepareFormData(model);
    model.addAttribute("department", new CreateDepartmentDto());

    return "admin/department/add";
  }

  @PostMapping("/add")
  public String add(@Valid @ModelAttribute("department") CreateDepartmentDto departmentDto,
      BindingResult result,
      RedirectAttributes redirectAttributes,
      Model model) {

    if (result.hasErrors()) {
      prepareFormData(model);
      model.addAttribute("department", departmentDto);
      return "admin/department/add";
    }
    departmentService.createDepartment(departmentDto);

    redirectAttributes.addFlashAttribute("success", i18nUtil.get("admin.department.created_success"));
    return "redirect:/admin/departments";
  }

  @GetMapping("/{id}/edit")
  public String getEditForm(@PathVariable Long id, Model model) {
    EditDepartmentDto department = departmentService.getDepartmentToEdit(id);

    prepareFormData(model);
    model.addAttribute("department", department);

    return "admin/department/edit";
  }

  @PutMapping("/{id}/edit")
  public String edit(@PathVariable Long id,
      @Valid @ModelAttribute("department") EditDepartmentDto departmentDto,
      BindingResult result,
      RedirectAttributes redirectAttributes,
      Model model) {

    if (result.hasErrors()) {
      prepareFormData(model);
      model.addAttribute("department", departmentDto);

      return "admin/department/edit";
    }

    departmentService.updateDepartment(id, departmentDto);

    redirectAttributes.addFlashAttribute("success", i18nUtil.get("admin.department.updated_success"));
    return "redirect:/admin/departments";
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    departmentService.deleteDepartment(id);

    redirectAttributes.addFlashAttribute("success", i18nUtil.get("admin.department.deleted_success"));
    return "redirect:/admin/departments";
  }

  private void prepareFormData(Model model) {
    model.addAttribute("leaders", departmentService.getAllManager());
  }
}
