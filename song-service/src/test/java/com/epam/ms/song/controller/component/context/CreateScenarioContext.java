package com.epam.ms.song.controller.component.context;

import com.epam.ms.song.model.Song;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreateScenarioContext {

  private Song request;
  private ResponseEntity<Map> response;

  public Song getRequest() {
    return request;
  }

  public void setRequest(Song request) {
    this.request = request;
  }

  public ResponseEntity<Map> getResponse() {
    return response;
  }

  public void setResponse(ResponseEntity<Map> response) {
    this.response = response;
  }
}
