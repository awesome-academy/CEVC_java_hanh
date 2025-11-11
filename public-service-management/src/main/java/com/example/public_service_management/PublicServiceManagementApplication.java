package com.example.public_service_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PublicServiceManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(PublicServiceManagementApplication.class, args);
  }

}
