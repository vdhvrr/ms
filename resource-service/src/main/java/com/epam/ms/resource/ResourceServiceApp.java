package com.epam.ms.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

@EnableCaching
@EnableRetry
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class ResourceServiceApp {

  public static void main(String[] args) {
    SpringApplication.run(ResourceServiceApp.class, args);
  }
}
