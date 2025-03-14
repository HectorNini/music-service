package ru.ispo.music_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ispo.music_service.entity.Pricing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PricingRepository extends JpaRepository<Pricing, Integer> {

    // Поиск действующей цены для трека на текущий момент
    @Query("SELECT p FROM Pricing p " +
            "WHERE p.track.trackId = :trackId " +
            "AND p.validFrom <= CURRENT_TIMESTAMP " +
            "AND p.validTo > CURRENT_TIMESTAMP")
    Optional<Pricing> findActiveByTrackId(@Param("trackId") Integer trackId);

    // Поиск действующей цены для плейлиста на текущий момент
    @Query("SELECT p FROM Pricing p " +
            "WHERE p.playlist.playlistId = :playlistId " +
            "AND p.validFrom <= CURRENT_TIMESTAMP " +
            "AND p.validTo > CURRENT_TIMESTAMP")
    Optional<Pricing> findActiveByPlaylistId(@Param("playlistId") Integer playlistId);

    // Поиск всех цен для трека (включая исторические)
    List<Pricing> findByTrack_TrackId(Integer trackId);

    // Поиск всех цен для плейлиста (включая исторические)
    List<Pricing> findByPlaylist_PlaylistId(Integer playlistId);

    // Поиск по уникальному идентификатору транзакции (если нужно)
    // Optional<Pricing> findByTransactionId(String transactionId);


}
