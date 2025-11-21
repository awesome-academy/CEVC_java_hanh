package com.example.public_service_management.admin.user;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.public_service_management.common.dto.PageReqDto;
import com.example.public_service_management.common.enums.Gender;
import com.example.public_service_management.common.enums.Role;
import com.example.public_service_management.common.enums.UserStatus;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.common.utils.PageableUtil;
import com.example.public_service_management.user.User;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/admin/users")
@AllArgsConstructor
public class UserController {
  private final UserService userService;
  private final I18nUtil i18nUtil;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
  }

  @GetMapping
  public String getList(
      Model model,
      @RequestParam(required = false) String role,
      @Valid @ModelAttribute PageReqDto pageReqDto) {

    List<String> roles = List.of(Role.staff.name(), Role.citizen.name());
    Pageable pageable = PageableUtil.toPageable(pageReqDto);
    Page<User> users = userService.getList(roles, role, pageable);

    model.addAttribute("roles", roles);
    model.addAttribute("role", role);
    model.addAttribute("users", users.getContent());
    model.addAttribute("currentPage", users.getNumber());
    model.addAttribute("totalPages", users.getTotalPages());

    return "admin/user/list";
  }

  @GetMapping("/{id}/details")
  public String details(@PathVariable Long id, Model model) {
    User user = userService.findUser(id);
    model.addAttribute("user", user);

    return "admin/user/details";
  }

  @GetMapping("/{id}/edit")
  public String getEditForm(@PathVariable Long id, Model model) {
    User user = userService.findUser(id);
    List<String> roles = List.of(Role.staff.name(), Role.citizen.name());

    model.addAttribute("user", user);
    model.addAttribute("roles", roles);
    model.addAttribute("genders", Gender.values());
    model.addAttribute("departments", userService.getAllDepartments());

    return "admin/user/edit";
  }

  @PostMapping("/{id}/edit")
  public String edit(@PathVariable Long id,
      @Valid @ModelAttribute("user") EditUserDto userDto,
      BindingResult result,
      RedirectAttributes redirectAttributes,
      Model model) {

    if (result.hasErrors()) {
      List<String> roles = List.of(Role.staff.name(), Role.citizen.name());

      model.addAttribute("user", userDto);
      model.addAttribute("roles", roles);
      model.addAttribute("genders", Gender.values());
      model.addAttribute("departments", userService.getAllDepartments());

      return "admin/user/edit";
    }

    userService.updateUser(id, userDto);

    redirectAttributes.addFlashAttribute("success", i18nUtil.get("admin.user.edit.user_updated_successfully"));
    return "redirect:/admin/users";
  }

  @GetMapping("/add")
  public String getAddForm(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("roles", List.of(Role.staff.name(), Role.citizen.name()));
    model.addAttribute("genders", Gender.values());
    model.addAttribute("departments", userService.getAllDepartments());

    return "admin/user/add";
  }

  @PostMapping("/add")
  public String add(@Valid @ModelAttribute("user") CreateUserDto userDto,
      BindingResult result,
      RedirectAttributes redirectAttributes,
      Model model) {

    if (result.hasErrors()) {
      model.addAttribute("user", userDto);
      model.addAttribute("roles", List.of(Role.staff.name(), Role.citizen.name()));
      model.addAttribute("genders", Gender.values());
      model.addAttribute("departments", userService.getAllDepartments());
      return "admin/user/add";
    }
    userService.createUser(userDto);

    redirectAttributes.addFlashAttribute("success", i18nUtil.get("admin.user.add.user_created_successfully"));
    return "redirect:/admin/users";
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    userService.deleteUser(id);

    redirectAttributes.addFlashAttribute("success", i18nUtil.get("admin.user.list.user_deleted_successfully"));
    return "redirect:/admin/users";
  }

  @PostMapping("/{id}/change-status")
  public String changeStatus(@PathVariable Long id,
      @RequestParam UserStatus status,
      RedirectAttributes redirectAttrs) {

    userService.changeStatus(id, status);

    String flashMsg = status == UserStatus.active
        ? i18nUtil.get("admin.user.list.user_unlocked_successfully")
        : i18nUtil.get("admin.user.list.user_locked_successfully");
    redirectAttrs.addFlashAttribute("success", flashMsg);
    return "redirect:/admin/users";
  }
}
