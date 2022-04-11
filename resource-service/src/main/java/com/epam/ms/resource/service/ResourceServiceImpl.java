package com.epam.ms.resource.service;

import com.amazonaws.services.s3.AmazonS3;
import com.epam.ms.resource.exception.ResourceException;
import com.epam.ms.resource.exception.ResourceNotFoundException;
import com.epam.ms.resource.model.Resource;
import com.epam.ms.resource.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceServiceImpl implements ResourceService {

  private final ResourceRepository resourceRepository;
  private final AmazonS3 s3Client;
  private final RabbitMQSender rabbitMqSender;

  @Value("${s3.bucket-name}")
  private String bucketName;

  public ResourceServiceImpl(
      ResourceRepository resourceRepository, AmazonS3 s3Client, RabbitMQSender rabbitMqSender) {
    this.resourceRepository = resourceRepository;
    this.s3Client = s3Client;
    this.rabbitMqSender = rabbitMqSender;
  }

  @Override
  public Resource saveAudioFile(MultipartFile multipartFile) {
    File file = convertMultipartToFile(multipartFile);
    if (file == null) {
      throw new ResourceException("Could not save file.");
    }

    s3Client.putObject(bucketName, multipartFile.getOriginalFilename(), file);
    Resource savedResource = resourceRepository.save(new Resource(file.getName()));
    rabbitMqSender.send(savedResource);

    return savedResource;
  }

  @Override
  public org.springframework.core.io.Resource getResource(long id) {
    Resource resource = resourceRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

    try {
      InputStream objectContent =
          s3Client.getObject(bucketName, resource.getName()).getObjectContent();
      File tmp = File.createTempFile("s3test", "");
      Files.copy(objectContent, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
      return new FileSystemResource(tmp);

    } catch (IOException e) {
      throw new ResourceException("Could not load resource." + e);
    }
  }

  @Override
  public List<Long> deleteResources(List<Long> ids) {
    List<Long> removedIds = new ArrayList<>();
    ids.stream()
        .map(resourceRepository::findById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .forEach(
            r -> {
              s3Client.deleteObject(bucketName, r.getName());
              removedIds.add(r.getId());
            });
    resourceRepository.deleteAllById(removedIds);
    return removedIds;
  }

  private File convertMultipartToFile(MultipartFile multipartFile) {
    try {
      File file = new File(multipartFile.getOriginalFilename());
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(multipartFile.getBytes());
      fos.close();
      return file;
    } catch (IOException ex) {
      return null;
    }
  }
}
