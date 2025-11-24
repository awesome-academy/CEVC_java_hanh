package com.example.public_service_management.common.config;

import java.util.Locale;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLocaleResolver implements LocaleResolver {
  public static final String LOCALE_SESSION = "SESSION_LOCALE";

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    Object sessionLocale = request.getSession().getAttribute(LOCALE_SESSION);
    if (sessionLocale instanceof Locale) {
      return (Locale) sessionLocale;
    }

    String langParam = request.getParameter("lang");
    if (langParam != null && !langParam.isEmpty()) {
      Locale locale = Locale.forLanguageTag(langParam);
      request.getSession().setAttribute(LOCALE_SESSION, locale);
      return locale;
    }

    String langHeader = request.getHeader("Accept-Language");
    if (langHeader != null && !langHeader.isEmpty()) {
      return Locale.forLanguageTag(langHeader);
    }

    return Locale.ENGLISH;
  }

  @Override
  public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    if (locale != null) {
      request.getSession().setAttribute(LOCALE_SESSION, locale);
    }
  }
}
