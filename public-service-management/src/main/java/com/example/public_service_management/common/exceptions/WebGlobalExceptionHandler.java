package com.example.public_service_management.common.exceptions;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.public_service_management.common.utils.I18nUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@ControllerAdvice(annotations = Controller.class)
@RequiredArgsConstructor
public class WebGlobalExceptionHandler {
  private final I18nUtil i18nUtil;

  @ExceptionHandler(BadRequestException.class)
  public String handleBadRequestException(BadRequestException ex, RedirectAttributes redirectAttributes,
      HttpServletRequest request) {

    redirectAttributes.addFlashAttribute("error", ex.getMessage());

    String referer = request.getHeader("Referer");
    if (referer != null && !referer.isEmpty()) {
      return "redirect:" + referer;
    }

    return "redirect:/admin/dashboard";
  }

  @ExceptionHandler(ConflictException.class)
  public String handleConflictException(ConflictException ex, RedirectAttributes redirectAttributes,
      HttpServletRequest request) {

    redirectAttributes.addFlashAttribute("error", ex.getMessage());

    String referer = request.getHeader("Referer");
    if (referer != null && !referer.isEmpty()) {
      return "redirect:" + referer;
    }

    return "redirect:/admin/dashboard";
  }

  @ExceptionHandler(UnauthorizedException.class)
  public String handleUnauthorizedException(UnauthorizedException ex, RedirectAttributes redirectAttributes,
      HttpServletRequest request) {

    redirectAttributes.addFlashAttribute("error", ex.getMessage());

    String referer = request.getHeader("Referer");
    if (referer != null && !referer.isEmpty()) {
      return "redirect:" + referer;
    }

    return "redirect:/admin/dashboard";
  }

  @ExceptionHandler(ForbiddenException.class)
  public String handleForbiddenException(ForbiddenException ex, RedirectAttributes redirectAttributes,
      HttpServletRequest request) {

    redirectAttributes.addFlashAttribute("error", ex.getMessage());

    String referer = request.getHeader("Referer");
    if (referer != null && !referer.isEmpty()) {
      return "redirect:" + referer;
    }

    return "redirect:/admin/dashboard";
  }

  @ExceptionHandler(NotFoundException.class)
  public String handleNotFoundException(NotFoundException ex, RedirectAttributes redirectAttributes,
      HttpServletRequest request) {

    redirectAttributes.addFlashAttribute("error", ex.getMessage());

    String referer = request.getHeader("Referer");
    if (referer != null && !referer.isEmpty()) {
      return "redirect:" + referer;
    }

    return "redirect:/admin/dashboard";
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public String handleValidationException(MethodArgumentNotValidException ex, RedirectAttributes redirectAttributes,
      HttpServletRequest request) {

    String flashMsg = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + " - " + error.getDefaultMessage())
        .collect(Collectors.joining("; ", "Validation errors occurred: ", ";"));
    redirectAttributes.addFlashAttribute("error", flashMsg);

    String referer = request.getHeader("Referer");
    if (referer != null && !referer.isEmpty()) {
      return "redirect:" + referer;
    }

    return "redirect:/admin/dashboard";
  }

  @ExceptionHandler(Exception.class)
  public String handleWebException(Exception ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    ex.printStackTrace();

    redirectAttributes.addFlashAttribute("error", i18nUtil.get("error.unexpected"));

    String referer = request.getHeader("Referer");
    if (referer != null && !referer.isEmpty()) {
      return "redirect:" + referer;
    }

    return "redirect:/admin/dashboard";
  }
}
