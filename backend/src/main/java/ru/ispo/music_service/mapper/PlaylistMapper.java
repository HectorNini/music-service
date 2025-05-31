package ru.ispo.music_service.mapper;

import org.springframework.stereotype.Component;
import ru.ispo.music_service.dto.PlaylistDto;
import ru.ispo.music_service.dto.TrackDto;
import ru.ispo.music_service.entity.Playlist;
import ru.ispo.music_service.entity.Track;
import ru.ispo.music_service.repository.PricingRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlaylistMapper {
    private final PricingRepository pricingRepository;

    public PlaylistMapper(PricingRepository pricingRepository) {
        this.pricingRepository = pricingRepository;
    }

    public PlaylistDto toDto(Playlist playlist, List<Track> tracks) {
        PlaylistDto dto = new PlaylistDto();
        dto.setPlaylistId(playlist.getPlaylistId());
        dto.setName(playlist.getName());
        dto.setDescription(playlist.getDescription());
        dto.setTracks(convertTracksToDto(tracks));

        pricingRepository.findActiveByPlaylistId(playlist.getPlaylistId())
                .ifPresent(pricing -> {
                    dto.setPriceId(pricing.getPriceId());
                    dto.setPrice(pricing.getPrice());
                });

        return dto;
    }

    private List<TrackDto> convertTracksToDto(List<Track> tracks) {
        return tracks.stream()
                .map(track -> {
                    TrackDto trackDto = new TrackDto();
                    trackDto.setTrackId(track.getTrackId());
                    trackDto.setTitle(track.getTitle());
                    trackDto.setArtist(track.getArtist());
                    trackDto.setDuration(track.getDuration());
                    
                    pricingRepository.findActiveByTrackId(track.getTrackId())
                            .ifPresent(pricing -> {
                                trackDto.setPriceId(pricing.getPriceId());
                                trackDto.setPrice(pricing.getPrice());
                            });
                    return trackDto;
                })
                .collect(Collectors.toList());
    }
} 