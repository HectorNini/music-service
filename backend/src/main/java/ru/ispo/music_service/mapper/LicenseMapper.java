package ru.ispo.music_service.mapper;

import org.springframework.stereotype.Component;
import ru.ispo.music_service.dto.LicenseDto;
import ru.ispo.music_service.dto.TrackDto;
import ru.ispo.music_service.entity.License;
import ru.ispo.music_service.entity.Track;
import ru.ispo.music_service.repository.PlaylistTrackRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LicenseMapper {
    private final PlaylistTrackRepository playlistTrackRepository;

    public LicenseMapper(PlaylistTrackRepository playlistTrackRepository) {
        this.playlistTrackRepository = playlistTrackRepository;
    }

    public LicenseDto toDto(License license) {
        LicenseDto dto = new LicenseDto();
        dto.setLicenseId(license.getLicenseId());
        dto.setEndDate(license.getEndDate());

        if (license.getPricing().getTrack() != null) {
            Track track = license.getPricing().getTrack();
            dto.setProductName(track.getTitle());
            dto.setTracks(List.of(convertTrackToDto(track)));
        } else if (license.getPricing().getPlaylist() != null) {
            dto.setProductName(license.getPricing().getPlaylist().getName());
            List<Track> tracks = playlistTrackRepository.findTracksByPlaylistId(
                license.getPricing().getPlaylist().getPlaylistId()
            );
            dto.setTracks(convertTracksToDto(tracks));
        }

        return dto;
    }

    private List<TrackDto> convertTracksToDto(List<Track> tracks) {
        return tracks.stream()
                .map(this::convertTrackToDto)
                .collect(Collectors.toList());
    }

    private TrackDto convertTrackToDto(Track track) {
        TrackDto trackDto = new TrackDto();
        trackDto.setTrackId(track.getTrackId());
        trackDto.setTitle(track.getTitle());
        trackDto.setArtist(track.getArtist());
        trackDto.setDuration(track.getDuration());
        return trackDto;
    }
} 