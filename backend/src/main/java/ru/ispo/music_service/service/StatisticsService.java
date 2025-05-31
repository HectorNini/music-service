package ru.ispo.music_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ispo.music_service.repository.LicenseRepository;
import ru.ispo.music_service.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final LicenseRepository licenseRepository;
    private final UserRepository userRepository;

    public List<Map<String, Object>> getTopTracks() {
        return licenseRepository.findTopTracks(10)
                .stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getTopPlaylists() {
        return licenseRepository.findTopPlaylists(10)
                .stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    public Long getUserCount() {
        return userRepository.count();
    }
} 