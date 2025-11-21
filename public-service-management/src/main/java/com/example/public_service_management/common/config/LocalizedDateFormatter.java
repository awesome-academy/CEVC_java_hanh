package com.example.public_service_management.common.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class LocalizedDateFormatter implements Formatter<LocalDate>, MessageSourceAware {
  private MessageSource messageSource;

  @Override
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public LocalDate parse(String text, Locale locale) {
    String pattern = messageSource.getMessage("date.format", null, locale);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    return LocalDate.parse(text, formatter);
  }

  @Override
  public String print(LocalDate object, Locale locale) {
    String pattern = messageSource.getMessage("date.format", null, locale);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    return formatter.format(object);
  }
}
