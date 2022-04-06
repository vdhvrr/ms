package com.epam.ms.resource.service;

import com.epam.ms.resource.model.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResourceService {

  Resource saveAudioFile(MultipartFile file);

  org.springframework.core.io.Resource getResource(long id);

  List<Long> deleteResources(List<Long> ids);
}
