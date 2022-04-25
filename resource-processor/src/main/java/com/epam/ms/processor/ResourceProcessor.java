package com.epam.ms.processor;

import com.epam.ms.processor.client.APIClient;
import com.epam.ms.processor.metadata.SongMetadataParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ResourceProcessor {

  private final Logger logger = LoggerFactory.getLogger(ResourceProcessor.class);

  private final APIClient apiClient;
  private final SongMetadataParser metadataParser;

  public ResourceProcessor(SongMetadataParser metadataParser, APIClient apiClient) {
    this.metadataParser = metadataParser;
    this.apiClient = apiClient;
  }

  @RabbitListener(queues = "${spring.rabbitmq.queue}")
  public void receiveMessage(String resourceId) {
    try {
      Resource resource = apiClient.retrieveSongBytes(resourceId);
      Map<String, String> metadataList = metadataParser.parseSongMetadata(resourceId, resource);
      apiClient.saveSongMetadata(metadataList);
      apiClient.moveToPermanentStorage(resourceId);
    } catch (Exception ex) {
      logger.error("receiveMessage error: ", ex);
    }
  }
}
