package com.epam.ms.resource.exception;

public class ResourceException extends RuntimeException {

  public ResourceException() {}

  public ResourceException(String message) {
    super(message);
  }
}
