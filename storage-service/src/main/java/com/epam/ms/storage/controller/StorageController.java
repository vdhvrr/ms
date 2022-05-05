package com.epam.ms.storage.controller;

import com.epam.ms.storage.model.Storage;
import com.epam.ms.storage.repository.StorageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/storages")
public class StorageController {

  private final StorageRepository storageRepository;

  public StorageController(StorageRepository storageRepository) {
    this.storageRepository = storageRepository;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Long>> create(@RequestBody @Valid Storage storage) {
    Storage createdStorage = storageRepository.save(storage);
    return ResponseEntity.ok(Collections.singletonMap("id", createdStorage.getId()));
  }

  @GetMapping
  public ResponseEntity<List<Storage>> getAll() {
    return ResponseEntity.ok(storageRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Storage> get(@PathVariable("id") long id) {
    return ResponseEntity.ok(storageRepository.findById(id).orElse(null));
  }

  @DeleteMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Collection<Long>>> delete(@RequestParam("id") List<Long> ids) {
    List<Long> existingStoragesIds =
        ids.stream().filter(storageRepository::existsById).collect(Collectors.toList());
    existingStoragesIds.forEach(storageRepository::deleteById);
    return ResponseEntity.ok(Collections.singletonMap("ids", existingStoragesIds));
  }
}
