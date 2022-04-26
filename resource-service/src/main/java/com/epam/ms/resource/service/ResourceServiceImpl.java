package com.epam.ms.resource.service;

import com.amazonaws.services.s3.AmazonS3;
import com.epam.ms.resource.exception.ResourceException;
import com.epam.ms.resource.exception.ResourceNotFoundException;
import com.epam.ms.resource.model.Resource;
import com.epam.ms.resource.repository.ResourceRepository;
import com.epam.ms.resource.storage.Storage;
import com.epam.ms.resource.storage.StorageService;
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
  private final StorageService storageService;

  public ResourceServiceImpl(
      ResourceRepository resourceRepository,
      AmazonS3 s3Client,
      RabbitMQSender rabbitMqSender,
      StorageService storageService) {
    this.resourceRepository = resourceRepository;
    this.s3Client = s3Client;
    this.rabbitMqSender = rabbitMqSender;
    this.storageService = storageService;
  }

  @Override
  public Resource saveAudioFile(MultipartFile multipartFile) {
    File file = convertMultipartToFile(multipartFile);
    if (file == null) {
      throw new ResourceException("Could not save file.");
    }

    Storage storage = storageService.getStaging();
    String stagingBucket = storage.getBucket();

    s3Client.putObject(stagingBucket, multipartFile.getOriginalFilename(), file);

    Resource resource = new Resource(file.getName());
    resource.setStorageId(storage.getId());
    Resource savedResource = resourceRepository.save(resource);

    rabbitMqSender.send(savedResource);

    return savedResource;
  }

  @Override
  public void moveToPermanentStorage(long id) {
    Storage storage = storageService.getPermanent();
    Resource resource = resourceRepository.getById(id);

    try {
      s3Client.putObject(storage.getBucket(), resource.getName(), getResource(id).getFile());

    } catch (IOException e) {
      throw new ResourceException("Could not save resource." + e);
    }

    resource.setStorageId(storage.getId());
    resourceRepository.save(resource);
  }

  @Override
  public org.springframework.core.io.Resource getResource(long id) {
    Resource resource = resourceRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    String bucket = storageService.getStorage(resource.getStorageId()).getBucket();

    try {
      InputStream objectContent = s3Client.getObject(bucket, resource.getName()).getObjectContent();
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
              String bucket = storageService.getStorage(r.getStorageId()).getBucket();
              s3Client.deleteObject(bucket, r.getName());
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
