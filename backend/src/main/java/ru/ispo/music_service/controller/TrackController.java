package ru.ispo.music_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.ispo.music_service.dto.TrackCreateDto;
import ru.ispo.music_service.dto.TrackDto;
import ru.ispo.music_service.entity.User;
import ru.ispo.music_service.service.TrackService;
import ru.ispo.music_service.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<TrackDto>> getAllTracks() {
        return ResponseEntity.ok(trackService.getAllTracks());
    }

    @GetMapping("/{trackId}")
    public ResponseEntity<TrackDto> getTrackById(@PathVariable Integer trackId) {
        return ResponseEntity.ok(trackService.getTrackById(trackId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrackDto> createTrack(@RequestBody @Valid TrackCreateDto trackCreateDto) {
        return ResponseEntity.ok(trackService.createTrack(trackCreateDto));
    }

    @PutMapping("/{trackId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrackDto> updateTrack(
            @PathVariable Integer trackId,
            @RequestBody @Valid TrackCreateDto trackCreateDto) {
        return ResponseEntity.ok(trackService.updateTrack(trackId, trackCreateDto));
    }

    @DeleteMapping("/{trackId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTrack(@PathVariable Integer trackId) {
        trackService.deleteTrack(trackId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/playlist/{playlistId}")
    public ResponseEntity<List<TrackDto>> getTracksByPlaylistId(@PathVariable Integer playlistId) {
        return ResponseEntity.ok(trackService.getTracksByPlaylistId(playlistId));
    }

    @GetMapping("/licensed")
    public ResponseEntity<List<TrackDto>> getLicensedTracks(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(trackService.getLicensedTracks(user.getUserId()));
    }
}
