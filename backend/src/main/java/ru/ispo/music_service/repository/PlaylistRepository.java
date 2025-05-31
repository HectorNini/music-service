package ru.ispo.music_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispo.music_service.entity.Playlist;


import java.util.Optional;


public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Optional<Playlist> findByPlaylistId(Integer playlistId);
}
