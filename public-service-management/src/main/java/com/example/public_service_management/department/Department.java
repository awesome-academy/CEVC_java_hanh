package com.example.public_service_management.department;

import java.util.List;

import org.hibernate.annotations.SQLDelete;

import com.example.public_service_management.common.entity.BaseEntity;
import com.example.public_service_management.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "departments")
@SQLDelete(sql = "UPDATE departments SET deleted_at = UTC_TIMESTAMP(6) WHERE id = ?")
@Getter
@Setter
public class Department extends BaseEntity {
  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String code;

  private String name;

  @Column(columnDefinition = "TEXT")
  private String address;

  @ManyToOne
  @JoinColumn(name = "leader_id")
  private User leader;

  @OneToMany(mappedBy = "department")
  private List<User> users;

  public Department() {
  }

  public Department(String code, String name, String address, User leader) {
    this.code = code;
    this.name = name;
    this.address = address;
    this.leader = leader;
  }
}
