package com.example.public_service_management.common.dto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageResDto<T> {
  private List<T> content;
  private int pageNumber;
  private int pageSize;
  private long totalElements;
  private int totalPages;
  private String sortField;
  private String sortDir;

  public PageResDto(Page<T> pageData) {
    this.content = pageData.getContent();
    this.pageNumber = pageData.getNumber();
    this.pageSize = pageData.getSize();
    this.totalElements = pageData.getTotalElements();
    this.totalPages = pageData.getTotalPages();
    if (pageData.getSort().isSorted()) {
      Order order = pageData.getSort().toList().get(0);
      this.sortField = order.getProperty();
      this.sortDir = order.isAscending() ? "asc" : "desc";
    }
  }
}
