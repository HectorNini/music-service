package ru.ispo.music_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.ispo.music_service.entity.Playlist;
import ru.ispo.music_service.entity.PlaylistTrack;
import ru.ispo.music_service.entity.PlaylistTrackId;
import ru.ispo.music_service.entity.Track;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistTrackRepository extends JpaRepository<PlaylistTrack, Long> {
    @Query("SELECT pt.track FROM PlaylistTrack pt " +
            "WHERE pt.playlist.playlistId IN :playlistIds")
    List<Track> findTracksByPlaylistIds(@Param("playlistIds") List<Integer> playlistIds);

    @Query("SELECT pt.track FROM PlaylistTrack pt " +
            "WHERE pt.playlist.playlistId = :playlistId")
    List<Track> findTracksByPlaylistId(@Param("playlistId") Integer playlistId);

    List<PlaylistTrack> findByPlaylist(Playlist playlist);
    void deleteAllByPlaylist(Playlist playlist);
}

