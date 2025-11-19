package com.example.public_service_management.application;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
  boolean existsByCode(String code);
}
