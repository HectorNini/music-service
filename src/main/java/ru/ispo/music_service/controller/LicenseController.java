package ru.ispo.music_service.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.ispo.music_service.entity.License;
import ru.ispo.music_service.entity.User;
import ru.ispo.music_service.repository.UserRepository;
import ru.ispo.music_service.service.LicenseService;

import java.util.List;

@RestController
@RequestMapping("/api/licenses")
public class LicenseController {
    private final LicenseService licenseService;
    private final UserRepository userRepository;

    public LicenseController(LicenseService licenseService, UserRepository userRepository) {
        this.licenseService = licenseService;
        this.userRepository = userRepository;
    }

    @GetMapping("/active")
    public ResponseEntity<List<License>> getActiveLicenses(
            @AuthenticationPrincipal UserDetails userDetails) { // Используем UserDetails
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(licenseService.getActiveLicenses(user.getUserId()));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(
            @RequestBody License license,
            @AuthenticationPrincipal User user) {
        license.setUser(user);
        return ResponseEntity.ok(licenseService.createLicense(license));
    }
}