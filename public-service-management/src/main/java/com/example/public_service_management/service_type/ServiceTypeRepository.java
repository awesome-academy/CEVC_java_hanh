package com.example.public_service_management.service_type;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long>, JpaSpecificationExecutor<ServiceType> {
  @Query("SELECT st FROM ServiceType st WHERE (:categoryId is null or st.category.id = :categoryId)")
  Page<ServiceType> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

  @Override
  @EntityGraph(attributePaths = { "department", "category" })
  Page<ServiceType> findAll(Specification<ServiceType> spec, Pageable pageable);

  boolean existsByCode(String code);
}
