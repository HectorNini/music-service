package ru.ispo.music_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispo.music_service.entity.License;
import ru.ispo.music_service.entity.Playlist;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Optional<Playlist> findByPlaylistId(Integer playlistId);
}
