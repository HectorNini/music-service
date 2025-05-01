package ru.ispo.music_service.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.ispo.music_service.dto.PlaylistDto;
import ru.ispo.music_service.dto.TrackDto;
import ru.ispo.music_service.entity.Playlist;
import ru.ispo.music_service.entity.Track;
import ru.ispo.music_service.repository.PlaylistRepository;
import ru.ispo.music_service.repository.PlaylistTrackRepository;
import ru.ispo.music_service.repository.PricingRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final PricingRepository pricingRepository;
    private final ModelMapper modelMapper;

    public PlaylistService(PlaylistRepository playlistRepository,
                           PlaylistTrackRepository playlistTrackRepository, PricingRepository pricingRepository, ModelMapper modelMapper) {
        this.playlistRepository = playlistRepository;
        this.playlistTrackRepository = playlistTrackRepository;
        this.pricingRepository = pricingRepository;
        this.modelMapper = modelMapper;
    }

    public List<PlaylistDto> getAllPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();
        return playlists.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PlaylistDto convertToDto(Playlist playlist) {
        PlaylistDto dto = new PlaylistDto();
        dto.setPlaylistId(playlist.getPlaylistId());
        dto.setName(playlist.getName());
        dto.setDescription(playlist.getDescription());

        // Получаем связанные треки
        List<Track> tracks = playlistTrackRepository.findTracksByPlaylistId(playlist.getPlaylistId());
        dto.setTracks(tracks.stream()
                .map(track -> {
                    TrackDto trackDto = new TrackDto();
                    trackDto.setTrackId(track.getTrackId());
                    trackDto.setTitle(track.getTitle());
                    trackDto.setArtist(track.getArtist());
                    trackDto.setDuration(track.getDuration());
                    
                    // Добавляем информацию о цене трека
                    pricingRepository.findActiveByTrackId(track.getTrackId())
                            .ifPresent(pricing -> {
                                trackDto.setPriceId(pricing.getPriceId());
                                trackDto.setPrice(pricing.getPrice());
                            });
                    return trackDto;
                })
                .collect(Collectors.toList()));

        // Добавляем информацию о цене плейлиста
        pricingRepository.findActiveByPlaylistId(playlist.getPlaylistId())
                .ifPresent(pricing -> {
                    dto.setPriceId(pricing.getPriceId());
                    dto.setPrice(pricing.getPrice());
                });

        return dto;
    }
}