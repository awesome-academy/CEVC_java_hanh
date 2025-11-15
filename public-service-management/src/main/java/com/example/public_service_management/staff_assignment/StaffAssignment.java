package com.example.public_service_management.staff_assignment;

import org.hibernate.annotations.SQLDelete;

import com.example.public_service_management.common.entity.BaseEntity;
import com.example.public_service_management.service_type.ServiceType;
import com.example.public_service_management.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "staff_assignments")
@SQLDelete(sql = "UPDATE staff_assignments SET deleted_at = UTC_TIMESTAMP(6) WHERE id = ?")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffAssignment extends BaseEntity {
  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "staff_id", nullable = false)
  private User staff;

  @ManyToOne
  @JoinColumn(name = "service_type_id", nullable = false)
  private ServiceType serviceType;

  @ManyToOne
  @JoinColumn(name = "assigned_by_id", nullable = false)
  private User assignedBy;
}
