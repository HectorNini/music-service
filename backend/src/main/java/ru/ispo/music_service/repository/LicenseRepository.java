package ru.ispo.music_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ispo.music_service.entity.License;
import ru.ispo.music_service.entity.Track;

import java.time.LocalDate;
import java.util.List;
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
}
