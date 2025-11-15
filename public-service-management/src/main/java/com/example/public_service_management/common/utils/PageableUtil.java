package com.example.public_service_management.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.public_service_management.common.dto.PageReqDto;

public class PageableUtil {
  private PageableUtil() {
  }

  public static Pageable toPageable(PageReqDto dto) {
    int page = dto.getPage() != null ? dto.getPage() : 0;
    int size = dto.getSize() != null ? dto.getSize() : 10;
    String sortField = dto.getSortField() != null ? dto.getSortField() : "id";
    Sort sort = Sort.by(sortField);
    sort = "desc".equalsIgnoreCase(dto.getSortDir()) ? sort.descending() : sort.ascending();

    return PageRequest.of(page, size, sort);
  }
}
