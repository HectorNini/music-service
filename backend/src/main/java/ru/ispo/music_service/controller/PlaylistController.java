package ru.ispo.music_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ispo.music_service.dto.PlaylistDto;
import ru.ispo.music_service.service.PlaylistService;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }


    @GetMapping
    public ResponseEntity<List<PlaylistDto>> getAllPlaylists() {

        List<PlaylistDto> playlists = playlistService.getAllPlaylists();
        return ResponseEntity.ok(playlists);
    }
}
