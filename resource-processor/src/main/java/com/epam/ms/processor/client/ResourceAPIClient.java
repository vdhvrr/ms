package com.epam.ms.processor.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ResourceAPIClient {

  @Value("${api.resource-url}")
  private String resourcesApi;

  private final WebClient.Builder connectionBuilder;

  public ResourceAPIClient(WebClient.Builder connectionBuilder) {
    this.connectionBuilder = connectionBuilder;
  }

  @Retryable(
      value = Exception.class,
      maxAttemptsExpression = "${retry.maxAttempts}",
      backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
  public ByteArrayResource retrieveSongBytes(String resourceId) {
    return connectionBuilder
        .baseUrl(resourcesApi)
        .build()
        .get()
        .uri(resourceId)
        .retrieve()
        .bodyToMono(ByteArrayResource.class)
        .block();
  }
}
