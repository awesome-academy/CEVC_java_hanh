package com.example.public_service_management.common.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.public_service_management.common.exceptions.BadRequestException;
import com.example.public_service_management.common.exceptions.NotFoundException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileUtil {
  private final I18nUtil i18nUtil;

  private Path rootLocation;

  @Value("${file.upload-dir}")
  private String uploadDir;

  @Value("${file.max-size}")
  private long maxFileSize;

  @Value("${file.allowed-types}")
  private String allowedFileTypesConfig;
  private String[] allowedFileTypes;

  @Value("${file.allowed-extensions}")
  private String allowedFileExtensionsConfig;
  private String[] allowedFileExtensions;

  @PostConstruct
  public void init() {
    try {
      this.allowedFileTypes = allowedFileTypesConfig.split(",");
      this.allowedFileExtensions = allowedFileExtensionsConfig.split(",");

      this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String storeFile(MultipartFile file, String dirName) {
    try {
      String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

      String extension = "";
      String baseName = originalFileName;

      int i = originalFileName.lastIndexOf('.');
      if (i > 0) {
        extension = originalFileName.substring(i + 1).toLowerCase();
        baseName = originalFileName.substring(0, i);
      }

      validateFile(file.getSize(), extension, file.getContentType());

      Path targetDir = rootLocation.resolve(dirName).normalize();
      Files.createDirectories(targetDir);

      String uniqFileName = genUniqFileName(targetDir, baseName, extension);
      Path targetLocation = targetDir.resolve(uniqFileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      return dirName + "/" + uniqFileName;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Resource loadFileAsResource(String relativePath) {
    try {
      Path filePath = rootLocation.resolve(relativePath).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        return resource;
      }
      throw new NotFoundException(i18nUtil.get("error.file.not_found", relativePath));
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  private void validateFile(long size, String extension, String contentType) {
    if (size > maxFileSize) {
      throw new BadRequestException(i18nUtil.get("error.file.too_large", maxFileSize / 1024 / 1024));
    }

    if (Arrays.stream(allowedFileExtensions).noneMatch(ext -> ext.equalsIgnoreCase(extension))) {
      throw new BadRequestException(i18nUtil.get("error.file.invalid_extension", allowedFileExtensionsConfig));
    }

    if (Arrays.stream(allowedFileTypes).noneMatch(type -> type.equalsIgnoreCase(contentType))) {
      throw new BadRequestException(i18nUtil.get("error.file.invalid_content_type", allowedFileTypesConfig));
    }
  }

  private String genUniqFileName(Path targetDir, String baseName, String extension) {
    String uniqCode = CodeUtil.genUniqCode(baseName,
        code -> Files.exists(targetDir.resolve(baseName + "_" + code + "." + extension)));
    return baseName + "_" + uniqCode + "." + extension;
  }
}
