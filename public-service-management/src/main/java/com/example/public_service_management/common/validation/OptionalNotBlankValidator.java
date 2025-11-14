package com.example.public_service_management.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OptionalNotBlankValidator implements ConstraintValidator<OptionalNotBlank, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || !value.isBlank();
  }
}
