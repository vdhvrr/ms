package com.epam.ms.processor;

import com.epam.ms.processor.client.ResourceAPIClient;
import com.epam.ms.processor.client.SongAPIClient;
import com.epam.ms.processor.metadata.SongMetadataParser;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ResourceProcessor {

  private final ResourceAPIClient resourceAPI;
  private final SongAPIClient songAPI;
  private final SongMetadataParser metadataParser;

  public ResourceProcessor(
      ResourceAPIClient resourceAPIClient,
      SongMetadataParser metadataParser,
      SongAPIClient songAPIClient) {
    this.resourceAPI = resourceAPIClient;
    this.metadataParser = metadataParser;
    this.songAPI = songAPIClient;
  }

  @RabbitListener(queues = "${spring.rabbitmq.queue}")
  public void receiveMessage(String resourceId) throws Exception {
    ByteArrayResource resource = resourceAPI.retrieveSongBytes(resourceId);
    Map<String, String> metadataList = metadataParser.parseSongMetadata(resourceId, resource);
    songAPI.saveSongMetadata(metadataList);
  }
}
