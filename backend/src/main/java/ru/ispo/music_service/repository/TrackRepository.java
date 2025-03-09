package ru.ispo.music_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispo.music_service.entity.Role;
import ru.ispo.music_service.entity.Track;

import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {
    Optional<Track> findByTrackId(Integer trackId);
}
