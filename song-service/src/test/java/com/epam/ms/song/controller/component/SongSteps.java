package com.epam.ms.song.controller.component;

import com.epam.ms.song.controller.component.context.CreateScenarioContext;
import com.epam.ms.song.controller.component.context.DeleteScenarioContext;
import com.epam.ms.song.controller.component.context.GetScenarioContext;
import com.epam.ms.song.model.Song;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SongSteps {

  @Autowired private SongAPIClient songAPIClient;
  @Autowired private CreateScenarioContext createContext;
  @Autowired private GetScenarioContext getContext;
  @Autowired private DeleteScenarioContext deleteContext;

  @Given("a song to create")
  public void createSong() {
    Song song = new Song(123L, "Song", "Artist", "Album", "12:34", 2022, 1L);
    createContext.setRequest(song);
  }

  @When("user posts song")
  public void postSong() {
    ResponseEntity<Map> response = songAPIClient.create(createContext.getRequest());
    createContext.setResponse(response);
  }

  @Then("song is saved")
  public void saveSong() {
    Object id = createContext.getResponse().getBody().get("id");
    assertNotNull(songAPIClient.getById(id).getBody().getId());
  }

  @Then("song id returned")
  public void returnSongId() {
    assertEquals(HttpStatus.OK, createContext.getResponse().getStatusCode());
    assertNotNull(createContext.getResponse().getBody().get("id"));
  }

  @Given("saved song")
  public void getSavedSong() {
    Song song = new Song("Song", "Artist", "Album", "12:34", 2022, 1L);
    ResponseEntity<Map> response = songAPIClient.create(song);
    song.setId(Long.valueOf((Integer) response.getBody().get("id")));
    getContext.setSong(song);
  }

  @When("user gets song by id")
  public void requestSongById() {
    Long id = getContext.getSong().getId();
    getContext.setResponse(songAPIClient.getById(id));
  }

  @Then("song is returned")
  public void getSong() {
    assertEquals(HttpStatus.OK, getContext.getResponse().getStatusCode());
    assertEquals(getContext.getSong(), getContext.getResponse().getBody());
  }

  @Given("saved songs")
  public void getSavedSongs() {
    Song song = new Song("Song", "Artist", "Album", "12:34", 2022, 1L);
    Song song2 = new Song("Song2", "Artist2", "Album2", "12:34", 2022, 1L);

    ResponseEntity<Map> response = songAPIClient.create(song);
    Long id = Long.valueOf((Integer) response.getBody().get("id"));
    ResponseEntity<Map> response2 = songAPIClient.create(song2);
    Long id2 = Long.valueOf((Integer) response2.getBody().get("id"));
    deleteContext.setSongIds(List.of(id, id2));
  }

  @When("user deletes songs")
  public void deleteSongs() {
    List<Long> ids = deleteContext.getSongIds();
    try {
      songAPIClient.delete(ids);
      ResponseEntity<Map> response = new ResponseEntity<>(Map.of("ids", ids), HttpStatus.OK);
      deleteContext.setResponse(response);
    } catch (HttpClientErrorException ex) {
      String message = ex.getResponseBodyAsString();
      ResponseEntity<Map> response =
          new ResponseEntity<>(Map.of("message", message), HttpStatus.OK);
      deleteContext.setResponse(response);
    }
  }

  @Then("deleted songs ids are returned")
  public void getDeletedSongIds() {
    assertEquals(HttpStatus.OK, deleteContext.getResponse().getStatusCode());
    assertNotNull(deleteContext.getResponse().getBody().get("ids"));
  }
}
