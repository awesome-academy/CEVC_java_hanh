package com.example.public_service_management.staff_assignment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StaffAssignmentRepository extends JpaRepository<StaffAssignment, Long> {
  @Query("SELECT sa.staff.id, COUNT(a) " +
      "FROM StaffAssignment sa LEFT JOIN Application a ON a.staff = sa.staff " +
      "WHERE sa.serviceType.id = :serviceTypeId " +
      "GROUP BY sa.staff.id")
  List<Object[]> countApplicationsByStaff(@Param("serviceTypeId") Long serviceTypeId);
}
