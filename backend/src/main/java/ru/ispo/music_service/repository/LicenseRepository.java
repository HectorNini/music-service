package ru.ispo.music_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ispo.music_service.entity.License;
import ru.ispo.music_service.entity.Track;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LicenseRepository extends JpaRepository<License, Long> {
    Optional<License> findByLicenseId(Integer licenseId);
    List<License> findByUser_UserIdAndEndDateAfter(Integer userId, LocalDate date);
    @Query("SELECT l.pricing.playlist.playlistId " +
            "FROM License l " +
            "WHERE l.user.userId = :userId " +
            "AND l.endDate >= :currentDate")
    List<Integer> findActivePlaylistIds(@Param("userId") Integer userId,
                                        @Param("currentDate") LocalDate currentDate);

    @Query("SELECT t.title as title, COUNT(l) as count " +
           "FROM License l " +
           "JOIN l.pricing p " +
           "JOIN p.track t " +
           "GROUP BY t.title " +
           "ORDER BY count DESC")
    List<Map<String, Object>> findTopTracks(int limit);

    @Query("SELECT p.name as name, COUNT(l) as count " +
           "FROM License l " +
           "JOIN l.pricing pr " +
           "JOIN pr.playlist p " +
           "GROUP BY p.name " +
           "ORDER BY count DESC")
    List<Map<String, Object>> findTopPlaylists(int limit);
}
