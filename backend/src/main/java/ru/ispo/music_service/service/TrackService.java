package ru.ispo.music_service.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.ispo.music_service.dto.TrackDto;
import ru.ispo.music_service.entity.Track;
import ru.ispo.music_service.repository.LicenseRepository;
import ru.ispo.music_service.repository.PlaylistTrackRepository;
import ru.ispo.music_service.repository.PricingRepository;
import ru.ispo.music_service.repository.TrackRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackService {
    private final TrackRepository trackRepository;
    private final LicenseRepository licenseRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final PricingRepository pricingRepository;
    private final ModelMapper modelMapper;

    public TrackService(TrackRepository trackRepository,
                        LicenseRepository licenseRepository,
                        PlaylistTrackRepository playlistTrackRepository, PricingRepository pricingRepository, ModelMapper modelMapper) {
        this.trackRepository = trackRepository;
        this.licenseRepository = licenseRepository;
        this.playlistTrackRepository = playlistTrackRepository;
        this.pricingRepository = pricingRepository;
        this.modelMapper = modelMapper;
    }

    public List<TrackDto> getAllTracks() {
        List<Track> tracks = trackRepository.findAll();
        return tracks.stream().map(track -> {
            TrackDto dto = new TrackDto();
            // Маппинг базовых полей
            dto.setTrackId(track.getTrackId());
            dto.setTitle(track.getTitle());
            dto.setArtist(track.getArtist());
            dto.setDuration(track.getDuration());

            // Поиск актуальной цены
            pricingRepository.findActiveByTrackId(track.getTrackId())
                    .ifPresent(pricing -> {
                        dto.setPriceId(pricing.getPriceId());
                        dto.setPrice(pricing.getPrice());
                    });
            return dto;
        }).collect(Collectors.toList());
    }

    public List<TrackDto> getLicensedTracks(Integer userId) {
        // Треки из прямых лицензий
        List<Track> directTracks = trackRepository.findLicensedTracks(userId, LocalDate.now());

        // Треки из лицензированных плейлистов
        List<Integer> licensedPlaylistIds = licenseRepository.findActivePlaylistIds(userId, LocalDate.now());
        List<Track> playlistTracks = playlistTrackRepository.findTracksByPlaylistIds(licensedPlaylistIds);

        // Объединяем и удаляем дубликаты
        List<Track> allTracks = new ArrayList<>();
        allTracks.addAll(directTracks);
        allTracks.addAll(playlistTracks);
        allTracks = allTracks.stream()
                .distinct()
                .collect(Collectors.toList());

        return allTracks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TrackDto convertToDto(Track track) {
        TrackDto dto = modelMapper.map(track, TrackDto.class);
        // Добавляем информацию о цене трека
        pricingRepository.findActiveByTrackId(track.getTrackId())
                .ifPresent(pricing -> {
                    dto.setPriceId(pricing.getPriceId());
                    dto.setPrice(pricing.getPrice());
                });
        return dto;
    }
}
