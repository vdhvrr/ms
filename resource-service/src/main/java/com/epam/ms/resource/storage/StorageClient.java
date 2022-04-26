package com.epam.ms.resource.storage;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("api-gateway")
public interface StorageClient {

  @Retryable(
      value = Exception.class,
      maxAttemptsExpression = "${retry.maxAttempts}",
      backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
  @GetMapping("/storages")
  List<Storage> getStorages();

  @Retryable(
      value = Exception.class,
      maxAttemptsExpression = "${retry.maxAttempts}",
      backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
  @GetMapping("/storages/{id}")
  Storage getStorage(@PathVariable("id") long id);
}
