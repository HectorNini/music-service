package ru.ispo.music_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispo.music_service.entity.Track;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
