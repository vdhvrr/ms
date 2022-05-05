package com.epam.ms.resource.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class APIClient {

  private final RestTemplate restTemplate = new RestTemplate();

  @Autowired ObjectMapper objectMapper;

  @LocalServerPort private int port;

  public ResponseEntity<Map> postResource(Resource resource) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", resource);
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    return new RestTemplate()
        .postForEntity(
            String.format("http://localhost:" + port + "/resources"), requestEntity, Map.class);
  }

  public ResponseEntity<Map> getMetadata(Object id) {
    return restTemplate.getForEntity("http://localhost:8082/songs/" + id, Map.class);
  }
}
