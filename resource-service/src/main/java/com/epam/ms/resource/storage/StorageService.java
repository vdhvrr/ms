package com.epam.ms.resource.storage;

import com.epam.ms.resource.exception.ResourceException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

  private final Logger logger = LoggerFactory.getLogger(StorageService.class);

  private final StorageClient apiClient;
  private final CacheManager cacheManager;

  public StorageService(StorageClient apiClient, CacheManager cacheManager) {
    this.apiClient = apiClient;
    this.cacheManager = cacheManager;
  }

  @CachePut(value = "staging-storage", key = "#root.method.name")
  @CircuitBreaker(name = "storage", fallbackMethod = "getStagingFromCache")
  public Storage getStaging() {
    return apiClient.getStorages().stream()
        .filter(s -> "STAGING".equals(s.getStorageType()))
        .findAny()
        .orElseThrow(ResourceException::new);
  }

  @CachePut(value = "permanent-storage", key = "#root.method.name")
  @CircuitBreaker(name = "storage", fallbackMethod = "getPermanentFromCache")
  public Storage getPermanent() {
    return apiClient.getStorages().stream()
        .filter(s -> "PERMANENT".equals(s.getStorageType()))
        .findAny()
        .orElseThrow(ResourceException::new);
  }

  @CachePut(value = "storage", key = "#id")
  @CircuitBreaker(name = "storage", fallbackMethod = "getStorageFromCache")
  public Storage getStorage(long id) {
    return apiClient.getStorage(id);
  }

  private Storage getStagingFromCache(Throwable throwable) {
    logger.error("getStagingFromCache", throwable);
    return cacheManager.getCache("staging-storage").get("getStaging", Storage.class);
  }

  private Storage getPermanentFromCache(Throwable throwable) {
    logger.error("getPermanentFromCache", throwable);
    return cacheManager.getCache("permanent-storage").get("getPermanent", Storage.class);
  }

  private Storage getStorageFromCache(long id, Throwable throwable) {
    logger.error("getStorageFromCache", throwable);
    return cacheManager.getCache("storage").get(id, Storage.class);
  }
}
