package com.epam.ms.song.repository;

import com.epam.ms.song.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {}
