package com.epam.ms.song.exception;

public class SongNotFoundException extends RuntimeException {

  public SongNotFoundException() {
    super("Song not found");
  }
}
