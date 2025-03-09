package ru.ispo.music_service.service;


import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.ispo.music_service.dto.LicenseCreateDto;
import ru.ispo.music_service.dto.LicenseDto;
import ru.ispo.music_service.dto.PlaylistDto;
import ru.ispo.music_service.dto.TrackDto;
import ru.ispo.music_service.entity.License;
import ru.ispo.music_service.entity.Playlist;
import ru.ispo.music_service.entity.Track;
import ru.ispo.music_service.entity.User;
import ru.ispo.music_service.repository.LicenseRepository;
import ru.ispo.music_service.repository.PlaylistRepository;
import ru.ispo.music_service.repository.TrackRepository;
import ru.ispo.music_service.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LicenseService {
    private final LicenseRepository licenseRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    private final PlaylistRepository playlistRepository;

    public LicenseService(LicenseRepository licenseRepository,
                          ModelMapper modelMapper,
                          UserRepository userRepository,
                          TrackRepository trackRepository,
                          PlaylistRepository playlistRepository) {
        this.licenseRepository = licenseRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.trackRepository = trackRepository;
        this.playlistRepository = playlistRepository;
    }

    public List<LicenseDto> getActiveLicenses(Integer userId) {
        List<License> licenses = licenseRepository.findByUser_UserIdAndEndDateAfter(userId, LocalDate.now());
        return licenses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public LicenseDto createLicense(LicenseCreateDto licenseCreateDto, User user) {
        License license = modelMapper.map(licenseCreateDto, License.class);

        if (license.getTrack() == null && license.getPlaylist() == null) {
            throw new IllegalArgumentException("License must have track or playlist");
        }

        // Устанавливаем связи
        license.setUser(user);
        setTrackOrPlaylist(license, licenseCreateDto);

        License savedLicense = licenseRepository.save(license);
        return convertToDto(savedLicense);
    }

    private LicenseDto convertToDto(License license) {
        LicenseDto dto = modelMapper.map(license, LicenseDto.class);

        // Ручной маппинг для вложенных зависимостей
        if (license.getTrack() != null) {
            dto.setTrack(modelMapper.map(license.getTrack(), TrackDto.class));
        }
        if (license.getPlaylist() != null) {
            dto.setPlaylist(modelMapper.map(license.getPlaylist(), PlaylistDto.class));
        }

        return dto;
    }

    private void setTrackOrPlaylist(License license, LicenseCreateDto dto) {
        if (dto.getTrackId() != null) {
            Track track = trackRepository.findByTrackId(dto.getTrackId())
                    .orElseThrow(() -> new EntityNotFoundException("Track not found"));
            license.setTrack(track);
        }
        if (dto.getPlaylistId() != null) {
            Playlist playlist = playlistRepository.findByPlaylistId(dto.getPlaylistId())
                    .orElseThrow(() -> new EntityNotFoundException("Playlist not found"));
            license.setPlaylist(playlist);
        }
    }
}