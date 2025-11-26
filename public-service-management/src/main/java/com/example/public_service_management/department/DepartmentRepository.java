package com.example.public_service_management.department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
  @Override
  Page<Department> findAll(Pageable pageable);

  boolean existsByCode(String code);
}
