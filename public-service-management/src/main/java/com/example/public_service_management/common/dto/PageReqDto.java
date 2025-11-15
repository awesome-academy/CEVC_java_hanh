package com.example.public_service_management.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageReqDto {
  @Min(value = 0, message = "{validation.page.min}")
  private Integer page;

  @Min(value = 1, message = "{validation.size.min}")
  @Max(value = 100, message = "{validation.size.max}")
  private Integer size;

  private String sortField;

  private String sortDir;
}
