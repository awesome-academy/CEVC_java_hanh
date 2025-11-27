package com.example.public_service_management.admin.application;

import org.springframework.data.jpa.domain.Specification;

import com.example.public_service_management.application.Application;
import com.example.public_service_management.service_type.ServiceType;
import com.example.public_service_management.user.User;

public class ApplicationSpecification {
  public static Specification<Application> serviceTypeEqual(ServiceType serviceType) {
    return (root, query, cb) -> (serviceType == null) ? null : cb.equal(root.get("serviceType"), serviceType);
  }

  public static Specification<Application> statusEqual(String status) {
    return (root, query, cb) -> (status == null || status.isEmpty()) ? null : cb.equal(root.get("status"), status);
  }

  public static Specification<Application> citizenEqual(User citizen) {
    return (root, query, cb) -> (citizen == null) ? null : cb.equal(root.get("citizen"), citizen);
  }

  public static Specification<Application> restrictByRole(User user) {
    if (user.isSuperAdmin()) {
      return null;
    }

    if (user.isManager()) {
      return (root, query, cb) -> cb.equal(root.get("serviceType").get("department"), user.getDepartment());
    }

    return (root, query, cb) -> cb.equal(root.get("staff"), user);
  }

  public static Specification<Application> hasId(Long id) {
    return (root, query, cb) -> cb.equal(root.get("id"), id);
  }

  public static Specification<Application> buildForList(User user, ServiceType serviceType, String status,
      User citizen) {
    return Specification.allOf(
        serviceTypeEqual(serviceType),
        statusEqual(status),
        citizenEqual(citizen),
        restrictByRole(user));
  }

  public static Specification<Application> buildForDetail(User user, Long id) {
    return Specification.allOf(
        hasId(id),
        restrictByRole(user));
  }
}
