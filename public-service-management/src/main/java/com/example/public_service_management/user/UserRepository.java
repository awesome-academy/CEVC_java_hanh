package com.example.public_service_management.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByEmailAndIdNot(String email, Long id);

  boolean existsByNationalId(String nationalId);

  boolean existsByNationalIdAndIdNot(String nationalId, Long id);

  boolean existsByEidIdentifierAndIdNot(String eidIdentifier, Long id);

  @Query("SELECT u FROM User u WHERE u.role IN :roles")
  Page<User> findByRoles(@Param("roles") List<String> roles, Pageable pageable);
}
