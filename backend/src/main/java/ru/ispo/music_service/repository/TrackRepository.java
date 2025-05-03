package ru.ispo.music_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispo.music_service.entity.Track;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Integer> {
    Optional<Track> findByTrackId(Integer trackId);
    @Query("SELECT t FROM Track t " +
            "JOIN Pricing p ON t.trackId = p.track.trackId " +
            "JOIN License l ON p.priceId = l.pricing.priceId " +
            "WHERE l.user.userId = :userId " +
            "AND l.endDate >= :currentDate")
    List<Track> findLicensedTracks(@Param("userId") Integer userId,
                                   @Param("currentDate") LocalDate currentDate);
}


