package com.epam.ms.song.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Song {

  @Id
  @GeneratedValue(generator = "song_id_seq")
  private Long id;

  @NotBlank @Column private String name;

  @NotBlank @Column private String artist;

  @Column private String album;

  @NotBlank @Column private String length;

  @NotNull @Column private Integer year;

  @NotNull @Column private Long resourceId;

  public Song() {}

  public Song(
      String name, String artist, String album, String length, Integer year, Long resourceId) {
    this.name = name;
    this.artist = artist;
    this.album = album;
    this.length = length;
    this.year = year;
    this.resourceId = resourceId;
  }

  public Song(
      Long id,
      String name,
      String artist,
      String album,
      String length,
      Integer year,
      Long resourceId) {
    this.id = id;
    this.name = name;
    this.artist = artist;
    this.album = album;
    this.length = length;
    this.year = year;
    this.resourceId = resourceId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public String getLength() {
    return length;
  }

  public void setLength(String length) {
    this.length = length;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public Long getResourceId() {
    return resourceId;
  }

  public void setResourceId(Long resourceId) {
    this.resourceId = resourceId;
  }
}
