package ru.ispo.music_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ispo.music_service.dto.TrackDto;
import ru.ispo.music_service.entity.User;
import ru.ispo.music_service.repository.UserRepository;
import ru.ispo.music_service.service.TrackService;
import ru.ispo.music_service.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {
    private final TrackService trackService;
    private final UserService userService;

    public TrackController(TrackService trackService, UserService userService) {
        this.trackService = trackService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<TrackDto>> getAllTracks() {
        List<TrackDto> tracks = trackService.getAllTracks();
        return ResponseEntity.ok(tracks);
    }


    @GetMapping("/licensed")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TrackDto>> getLicensedTracks(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername());
        List<TrackDto> tracks = trackService.getLicensedTracks(user.getUserId());
        return ResponseEntity.ok(tracks);
    }
}
