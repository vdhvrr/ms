package com.epam.ms.song.controller.component.context;

import com.epam.ms.song.model.Song;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class GetScenarioContext {

  private Song song;
  private ResponseEntity<Song> response;

  public Song getSong() {
    return song;
  }

  public void setSong(Song song) {
    this.song = song;
  }

  public ResponseEntity<Song> getResponse() {
    return response;
  }

  public void setResponse(ResponseEntity<Song> response) {
    this.response = response;
  }
}
