package com.epam.ms.processor.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient("api-gateway")
public interface APIClient {

  @Retryable(
          value = Exception.class,
          maxAttemptsExpression = "${retry.maxAttempts}",
          backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
  @GetMapping("/resources/{id}")
  Resource retrieveSongBytes(@PathVariable("id") String id);

  @Retryable(
      value = Exception.class,
      maxAttemptsExpression = "${retry.maxAttempts}",
      backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
  @PostMapping("/songs")
  void saveSongMetadata(Map<String, String> metadata);
}
