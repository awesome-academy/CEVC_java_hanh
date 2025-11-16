package com.example.public_service_management.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByEmailAndIdNot(String email, Long id);

  boolean existsByNationalId(String nationalId);
}
