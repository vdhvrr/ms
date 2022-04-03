package com.epam.ms.resource.controller;

import com.epam.ms.resource.service.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
  public ResponseEntity<Map<String, Long>> createResource(@RequestParam MultipartFile file) {
    Map<String, Long> created = resourceService.saveAudioFile(file);
    return ResponseEntity.ok(created);
  }

  @GetMapping("{id}")
  public ResponseEntity<org.springframework.core.io.Resource> getResource(@PathVariable long id) {
    org.springframework.core.io.Resource resource = resourceService.getResource(id);
    return ResponseEntity.ok(resource);
  }

  @DeleteMapping
  public ResponseEntity<Map<String, List<Long>>> deleteResource(
      @RequestParam(name = "id") List<Long> ids) {
    List<Long> deletedIds = resourceService.deleteResources(ids);
    return ResponseEntity.ok(Map.of("ids", deletedIds));
  }
}
