package ru.ispo.music_service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ispo.music_service.dto.TrackCreateDto;
import ru.ispo.music_service.dto.TrackDto;
import ru.ispo.music_service.entity.License;
import ru.ispo.music_service.entity.Track;
import ru.ispo.music_service.mapper.TrackMapper;
import ru.ispo.music_service.repository.LicenseRepository;
import ru.ispo.music_service.repository.PlaylistTrackRepository;
import ru.ispo.music_service.repository.TrackRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public interface TrackService {
    List<TrackDto> getAllTracks();
    TrackDto getTrackById(Integer trackId);
    TrackDto createTrack(TrackCreateDto trackCreateDto);
    TrackDto updateTrack(Integer trackId, TrackCreateDto trackCreateDto);
    void deleteTrack(Integer trackId);
    List<TrackDto> getTracksByPlaylistId(Integer playlistId);
    List<TrackDto> getLicensedTracks(Integer userId);
    TrackDto convertToDto(Track track);
}

@Service
@RequiredArgsConstructor
class TrackServiceImpl implements TrackService {
    private final TrackRepository trackRepository;
    private final LicenseRepository licenseRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final TrackMapper trackMapper;

    @Override
    public List<TrackDto> getAllTracks() {
        return trackRepository.findAll().stream()
                .map(trackMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TrackDto getTrackById(Integer trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new EntityNotFoundException("Track not found"));
        return trackMapper.toDto(track);
    }

    @Override
    @Transactional
    public TrackDto createTrack(TrackCreateDto trackCreateDto) {
        Track track = new Track();
        track.setTitle(trackCreateDto.getTitle());
        track.setArtist(trackCreateDto.getArtist());
        track.setDuration(trackCreateDto.getDuration());
        track.setFilePath(trackCreateDto.getFilePath());

        Track savedTrack = trackRepository.save(track);
        return trackMapper.toDto(savedTrack);
    }

    @Override
    @Transactional
    public TrackDto updateTrack(Integer trackId, TrackCreateDto trackCreateDto) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new EntityNotFoundException("Track not found"));

        track.setTitle(trackCreateDto.getTitle());
        track.setArtist(trackCreateDto.getArtist());
        track.setDuration(trackCreateDto.getDuration());
        track.setFilePath(trackCreateDto.getFilePath());

        Track updatedTrack = trackRepository.save(track);
        return trackMapper.toDto(updatedTrack);
    }

    @Override
    @Transactional
    public void deleteTrack(Integer trackId) {
        if (!trackRepository.existsById(trackId)) {
            throw new EntityNotFoundException("Track not found");
        }
        trackRepository.deleteById(trackId);
    }

    @Override
    public List<TrackDto> getTracksByPlaylistId(Integer playlistId) {
        return playlistTrackRepository.findTracksByPlaylistId(playlistId).stream()
                .map(trackMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackDto> getLicensedTracks(Integer userId) {
        List<License> licenses = licenseRepository.findByUser_UserIdAndEndDateAfter(userId, LocalDate.now());
        return licenses.stream()
                .filter(license -> license.getPricing().getTrack() != null)
                .map(license -> trackMapper.toDto(license.getPricing().getTrack()))
                .collect(Collectors.toList());
    }

    @Override
    public TrackDto convertToDto(Track track) {
        return trackMapper.toDto(track);
    }
} 