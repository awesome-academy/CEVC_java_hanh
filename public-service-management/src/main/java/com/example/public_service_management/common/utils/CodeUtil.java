package com.example.public_service_management.common.utils;

import java.util.function.Predicate;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class CodeUtil {
  public static String genUniqCode(String prefix, Predicate<String> existsFunc) {
    String code;

    do {
      code = prefix + "-" + RandomStringUtils.randomAlphanumeric(6);
    } while (existsFunc.test(code));

    return code;
  }
}
