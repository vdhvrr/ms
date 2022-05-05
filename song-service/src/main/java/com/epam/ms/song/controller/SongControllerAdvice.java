package com.epam.ms.song.controller;

import com.epam.ms.song.exception.SongNotFoundException;
import org.springframework.http.HttpStatus;
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
public class SongControllerAdvice {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<String> argumentMismatch(MethodArgumentTypeMismatchException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(SongNotFoundException.class)
  public ResponseEntity<String> notFound(SongNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> internalError(RuntimeException ex) {
    return ResponseEntity.internalServerError().body("Internal error: " + ex.getMessage());
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
