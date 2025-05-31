package ru.ispo.music_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ispo.music_service.service.StatisticsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/top-tracks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getTopTracks() {
        return ResponseEntity.ok(statisticsService.getTopTracks());
    }

    @GetMapping("/top-playlists")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getTopPlaylists() {
        return ResponseEntity.ok(statisticsService.getTopPlaylists());
    }

    @GetMapping("/user-count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getUserCount() {
        return ResponseEntity.ok(statisticsService.getUserCount());
    }
} 