package com.epam.ms.song.controller.component;

import com.epam.ms.song.model.Song;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class SongAPIClient {

  private final RestTemplate restTemplate = new RestTemplate();
  @LocalServerPort private int port;

  private String getSongsEndpoint() {
    return "http://localhost" + ":" + port + "/songs";
  }

  public ResponseEntity<Map> create(Song song) {
    return restTemplate.postForEntity(getSongsEndpoint(), song, Map.class);
  }

  public ResponseEntity<Song> getById(Object id) {
    return restTemplate.getForEntity(getSongsEndpoint() + "/" + id, Song.class);
  }

  public void delete(List<Long> ids) {
    String param = ids.stream().map(Object::toString).collect(Collectors.joining(","));
    UriComponents builder =
        UriComponentsBuilder.fromUriString(getSongsEndpoint()).queryParam("id", param).build();
    restTemplate.delete(builder.toUriString(), Map.of("ids", param));
  }
}
