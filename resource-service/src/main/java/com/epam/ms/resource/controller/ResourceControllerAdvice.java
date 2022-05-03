package com.epam.ms.resource.controller;

import com.epam.ms.resource.exception.ResourceException;
import com.epam.ms.resource.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class ResourceControllerAdvice {

  private final Logger logger = LoggerFactory.getLogger(ResourceControllerAdvice.class);

  @ExceptionHandler(ResourceException.class)
  public ResponseEntity<String> badRequest(ResourceException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> notFound(ResourceNotFoundException ex) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> internalError(RuntimeException ex) {
    logger.error("Internal error: ", ex);
    return ResponseEntity.internalServerError().body("Internal error: "+ ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<String> argumentMismatch(MethodArgumentTypeMismatchException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> validationError(MethodArgumentNotValidException ex) {
    Map<String, String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.toMap(
                    FieldError::getField,
                    e -> Optional.ofNullable(e.getDefaultMessage()).orElse("Validation error")));
    return ResponseEntity.badRequest().body(errors);
  }
}
