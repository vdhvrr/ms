package com.epam.ms.processor.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class SongAPIClient {

  @Value("${api.song-url}")
  private String songsApi;

  private final WebClient.Builder connectionBuilder;

  public SongAPIClient(WebClient.Builder connectionBuilder) {
    this.connectionBuilder = connectionBuilder;
  }

  @Retryable(
      value = Exception.class,
      maxAttemptsExpression = "${retry.maxAttempts}",
      backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
  public void saveSongMetadata(Map<String, String> metadata) {
    connectionBuilder
        .baseUrl(songsApi)
        .build()
        .post()
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(BodyInserters.fromValue(metadata))
        .retrieve()
        .bodyToMono(String.class)
        .block();
  }
}
