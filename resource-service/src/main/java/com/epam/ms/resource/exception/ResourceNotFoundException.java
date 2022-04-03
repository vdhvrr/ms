package com.epam.ms.resource.exception;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException() {
    super("Resource does not exist with given id");
  }
}
