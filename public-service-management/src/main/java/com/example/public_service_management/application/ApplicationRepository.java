package com.example.public_service_management.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
  boolean existsByCode(String code);

  @EntityGraph(attributePaths = { "serviceType" })
  Page<Application> findByCitizenId(Long citizenId, Pageable pageable);
}
