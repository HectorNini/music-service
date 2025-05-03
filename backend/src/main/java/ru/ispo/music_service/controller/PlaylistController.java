package ru.ispo.music_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ispo.music_service.dto.PlaylistCreateDto;
import ru.ispo.music_service.dto.PlaylistDto;
import ru.ispo.music_service.service.PlaylistService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<List<PlaylistDto>> getAllPlaylists() {
        List<PlaylistDto> playlists = playlistService.getAllPlaylists();
        return ResponseEntity.ok(playlists);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlaylistDto> createPlaylist(@RequestBody PlaylistCreateDto playlistCreateDto) {
        PlaylistDto playlist = playlistService.createPlaylist(playlistCreateDto);
        return ResponseEntity.ok(playlist);
    }

    @PostMapping("/suggest-price")
    public ResponseEntity<Map<String, BigDecimal>> suggestPrice(@RequestBody List<Integer> trackIds) {
        BigDecimal suggestedPrice = playlistService.calculateSuggestedPrice(trackIds);
        return ResponseEntity.ok(Map.of("suggestedPrice", suggestedPrice));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.ok().build();
    }
}
