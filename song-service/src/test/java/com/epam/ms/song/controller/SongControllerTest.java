package com.epam.ms.song.controller;

import com.epam.ms.song.exception.SongNotFoundException;
import com.epam.ms.song.model.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SongControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private SongController songController;

  @Test
  public void create_songCreated() throws Exception {
    // given
    Song song = new Song("Song", "Artist", "Album", "12:34", 2022, 1L);
    given(songController.create(song))
        .willReturn(ResponseEntity.ok(Collections.singletonMap("id", 123L)));

    // when
    // then
    this.mockMvc
        .perform(
            post("/songs")
                .content(objectMapper.writeValueAsString(song))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void get_songExists_songReturned() throws Exception {
    // given
    Song song = new Song(123L, "Song", "Artist", "Album", "12:34", 2022, 1L);
    given(songController.get(song.getId())).willReturn(ResponseEntity.ok(song));

    // when
    // then
    this.mockMvc
        .perform(get("/songs/{id}", 123L))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(song)));
  }

  @Test
  public void get_songDoesNotExist_404Returned() throws Exception {
    // given
    given(songController.get(123L)).willThrow(SongNotFoundException.class);

    // when
    // then
    this.mockMvc.perform(get("/songs/{id}", 123L)).andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void delete_returnOnlyDeleted() throws Exception {
    // given
    given(songController.delete(Arrays.asList(1L, 2L)))
        .willReturn(ResponseEntity.ok(Collections.singletonMap("ids", List.of(1L))));

    // when
    // then
    this.mockMvc
        .perform(delete("/songs").param("id", "1,2").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .string(
                    objectMapper.writeValueAsString(Collections.singletonMap("ids", List.of(1L)))));
  }
}
