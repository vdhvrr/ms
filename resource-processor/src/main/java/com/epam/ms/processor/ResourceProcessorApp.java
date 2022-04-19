package com.epam.ms.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

@EnableFeignClients
@EnableEurekaClient
@EnableRetry
@SpringBootApplication
public class ResourceProcessorApp {

  public static void main(String[] args) {
    SpringApplication.run(ResourceProcessorApp.class, args);
  }
}
