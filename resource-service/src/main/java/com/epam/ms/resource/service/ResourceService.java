package com.epam.ms.resource.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ResourceService {

  Map<String, Long> saveAudioFile(MultipartFile file);

  org.springframework.core.io.Resource getResource(long id);

  List<Long> deleteResources(List<Long> ids);
}
