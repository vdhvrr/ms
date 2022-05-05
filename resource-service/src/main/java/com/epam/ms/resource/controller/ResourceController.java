package com.epam.ms.resource.controller;

import com.epam.ms.resource.model.Resource;
import com.epam.ms.resource.service.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequestMapping("/resources")
@RestController
public class ResourceController {

  private final ResourceService resourceService;

  public ResourceController(ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  @PostMapping
  public ResponseEntity<Map<String, Long>> createResource(@RequestParam("file") MultipartFile file) {
    Resource resource = resourceService.saveAudioFile(file);
    return ResponseEntity.ok(Collections.singletonMap("id", resource.getId()));
  }

  @GetMapping("{id}")
  public ResponseEntity<org.springframework.core.io.Resource> getResource(@PathVariable long id) {
    org.springframework.core.io.Resource resource = resourceService.getResource(id);
    return ResponseEntity.ok(resource);
  }

  @PutMapping("/{id}/actions/move-to-permanent")
  public ResponseEntity<?> moveToPermanent(@PathVariable long id) {
    resourceService.moveToPermanentStorage(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  public ResponseEntity<Map<String, List<Long>>> deleteResource(
      @RequestParam(name = "id") List<Long> ids) {
    List<Long> deletedIds = resourceService.deleteResources(ids);
    return ResponseEntity.ok(Map.of("ids", deletedIds));
  }
}
