package ru.ispo.music_service.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ispo.music_service.dto.PlaylistCreateDto;
import ru.ispo.music_service.dto.PlaylistDto;
import ru.ispo.music_service.dto.TrackDto;
import ru.ispo.music_service.entity.Playlist;
import ru.ispo.music_service.entity.PlaylistTrack;
import ru.ispo.music_service.entity.PlaylistTrackId;
import ru.ispo.music_service.entity.Pricing;
import ru.ispo.music_service.entity.Track;
import ru.ispo.music_service.repository.PlaylistRepository;
import ru.ispo.music_service.repository.PlaylistTrackRepository;
import ru.ispo.music_service.repository.PricingRepository;
import ru.ispo.music_service.repository.TrackRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final PricingRepository pricingRepository;
    private final TrackRepository trackRepository;
    private final ModelMapper modelMapper;

    public PlaylistService(PlaylistRepository playlistRepository,
                           PlaylistTrackRepository playlistTrackRepository, 
                           PricingRepository pricingRepository, 
                           TrackRepository trackRepository,
                           ModelMapper modelMapper) {
        this.playlistRepository = playlistRepository;
        this.playlistTrackRepository = playlistTrackRepository;
        this.pricingRepository = pricingRepository;
        this.trackRepository = trackRepository;
        this.modelMapper = modelMapper;
    }

    public List<PlaylistDto> getAllPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();
        return playlists.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PlaylistDto createPlaylist(PlaylistCreateDto playlistCreateDto) {
        Playlist playlist = new Playlist();
        playlist.setName(playlistCreateDto.getName());
        playlist.setDescription(playlistCreateDto.getDescription());
        
        playlist = playlistRepository.save(playlist);

        // Добавляем треки в плейлист
        if (playlistCreateDto.getTrackIds() != null && !playlistCreateDto.getTrackIds().isEmpty()) {
            for (int i = 0; i < playlistCreateDto.getTrackIds().size(); i++) {
                Integer trackId = playlistCreateDto.getTrackIds().get(i);
                Track track = trackRepository.findByTrackId(trackId)
                        .orElseThrow(() -> new IllegalArgumentException("Track not found with id: " + trackId));

                PlaylistTrack playlistTrack = new PlaylistTrack();
                PlaylistTrackId id = new PlaylistTrackId();
                id.setPlaylistId(playlist.getPlaylistId());
                id.setTrackId(trackId);
                playlistTrack.setId(id);
                playlistTrack.setPlaylist(playlist);
                playlistTrack.setTrack(track);
                playlistTrack.setPosition(i);
                playlistTrackRepository.save(playlistTrack);
            }
        }

        // Создаем цену для плейлиста
        if (playlistCreateDto.getPrice() != null) {
            Pricing pricing = new Pricing();
            pricing.setPlaylist(playlist);
            pricing.setPrice(playlistCreateDto.getPrice());
            pricing.setValidFrom(LocalDateTime.now());
            pricing.setValidTo(LocalDateTime.now().plusYears(1)); // Цена действительна 1 год
            pricingRepository.save(pricing);
        }

        return convertToDto(playlist);
    }

    public BigDecimal calculateSuggestedPrice(List<Integer> trackIds) {
        if (trackIds == null || trackIds.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return trackIds.stream()
                .map(trackId -> pricingRepository.findActiveByTrackId(trackId)
                        .map(Pricing::getPrice)
                        .orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    @Transactional
    public void deletePlaylist(Long id) {
        Playlist playlist = playlistRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Playlist not found"));
        
        // Удаляем все связи с треками
        playlistTrackRepository.deleteAllByPlaylist(playlist);
        
        // Удаляем цену плейлиста, если она есть
        pricingRepository.findActiveByPlaylistId(playlist.getPlaylistId())
            .ifPresent(pricingRepository::delete);
        
        // Удаляем сам плейлист
        playlistRepository.delete(playlist);
    }
}