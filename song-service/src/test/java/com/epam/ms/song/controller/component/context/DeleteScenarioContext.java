package com.epam.ms.song.controller.component.context;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DeleteScenarioContext {

  private List<Long> songIds;
  private ResponseEntity<Map> response;

  public List<Long> getSongIds() {
    return songIds;
  }

  public void setSongIds(List<Long> songIds) {
    this.songIds = songIds;
  }

  public ResponseEntity<Map> getResponse() {
    return response;
  }

  public void setResponse(ResponseEntity<Map> response) {
    this.response = response;
  }
}
