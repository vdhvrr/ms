package com.epam.ms.song.controller;

import com.epam.ms.song.exception.SongNotFoundException;
import com.epam.ms.song.model.Song;
import com.epam.ms.song.repository.SongRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/songs")
public class SongController {

  private final SongRepository songRepository;

  public SongController(SongRepository songRepository) {
    this.songRepository = songRepository;
  }

  @PostMapping
  public ResponseEntity<Map<String, Long>> create(@RequestBody @Valid Song song) {
    Song createdSong = songRepository.save(song);
    return ResponseEntity.ok(Collections.singletonMap("id", createdSong.getId()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Song> get(@PathVariable("id") Long id) {
    Song song = songRepository.findById(id).orElseThrow(SongNotFoundException::new);
    return ResponseEntity.ok(song);
  }

  @DeleteMapping
  public ResponseEntity<Map<String, Collection<Long>>> delete(@RequestParam("id") List<Long> ids) {
    List<Long> existingSongsIds =
        ids.stream().filter(songRepository::existsById).collect(Collectors.toList());
    songRepository.deleteAllById(existingSongsIds);
    return ResponseEntity.ok(Collections.singletonMap("ids", existingSongsIds));
  }
}
