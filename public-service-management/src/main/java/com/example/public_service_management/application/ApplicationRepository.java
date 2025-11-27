package com.example.public_service_management.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {
  boolean existsByCode(String code);

  @EntityGraph(attributePaths = { "serviceType" })
  Page<Application> findByCitizenId(Long citizenId, Pageable pageable);

  @EntityGraph(attributePaths = { "serviceType", "serviceType.department", "citizen", "staff" })
  Page<Application> findAll(Specification<Application> spec, Pageable pageable);
}
