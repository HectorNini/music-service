package ru.ispo.music_sevice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispo.music_sevice.entity.Track;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
