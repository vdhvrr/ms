package com.epam.ms.song.controller;

import com.epam.ms.song.SongServiceApp;
import com.epam.ms.song.model.Song;
import com.epam.ms.song.repository.SongRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SongServiceApp.class)
public class SongContractTest {

  @Autowired private SongController songController;

  @MockBean private SongRepository songRepository;

  @BeforeEach
  public void setUp() {
    RestAssuredMockMvc.standaloneSetup(songController);
    Song song = new Song(1L, "Song1", "Artist1", "Album1", "1:23", 2022, 123L);
    when(songRepository.findById(anyLong())).thenReturn(Optional.of(song));
    when(songRepository.existsById(anyLong())).thenReturn(true);
  }
}
