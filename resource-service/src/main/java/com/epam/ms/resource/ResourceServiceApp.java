package com.epam.ms.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ResourceServiceApp {

  public static void main(String[] args) {
    SpringApplication.run(ResourceServiceApp.class, args);
  }
}
