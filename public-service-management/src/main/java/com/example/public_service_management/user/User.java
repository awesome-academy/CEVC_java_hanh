package com.example.public_service_management.user;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.SQLDelete;

import com.example.public_service_management.common.entity.BaseEntity;
import com.example.public_service_management.common.enums.Gender;
import com.example.public_service_management.common.enums.Role;
import com.example.public_service_management.common.enums.Status;
import com.example.public_service_management.department.Department;
import com.example.public_service_management.user_session.UserSession;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = UTC_TIMESTAMP(6) WHERE id = ?")
@Getter
@Setter
public class User extends BaseEntity {
  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String fullName;

  private LocalDate dateOfBirth;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(unique = true)
  private String nationalId;

  @Column(columnDefinition = "TEXT")
  private String address;

  private String phoneNumber;

  @Column(unique = true)
  private String email;

  private String passwordDigest;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role = Role.citizen;

  @ManyToOne
  @JoinColumn(name = "department_id")
  private Department department;

  @Column(unique = true)
  private String eidIdentifier;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status = Status.active;

  @Column(nullable = false)
  private Boolean emailNotificationEnabled = true;

  @OneToMany(mappedBy = "leader", fetch = FetchType.LAZY)
  private List<Department> departments;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<UserSession> userSessions;

  public User() {
  }

  public User(String fullName, LocalDate dateOfBirth, Gender gender, String nationalId,
      String address, String phoneNumber, String email, String passwordDigest,
      Role role, Department department, String eidIdentifier, Status status,
      Boolean emailNotificationEnabled) {
    this.fullName = fullName;
    this.dateOfBirth = dateOfBirth;
    this.gender = gender;
    this.nationalId = nationalId;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.passwordDigest = passwordDigest;
    this.role = role;
    this.department = department;
    this.eidIdentifier = eidIdentifier;
    this.status = status;
    this.emailNotificationEnabled = emailNotificationEnabled;
  }

  public boolean isLocked() {
    return this.status == Status.locked;
  }

  public boolean isEnabled() {
    return this.status == Status.active && this.getDeletedAt() == null;
  }
}
