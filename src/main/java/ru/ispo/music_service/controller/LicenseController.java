package ru.ispo.music_service.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ispo.music_service.entity.License;
import ru.ispo.music_service.service.LicenseService;

import java.util.List;

@RestController
@RequestMapping("/api/licenses")
public class LicenseController {
    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping("/active")
    public ResponseEntity<List<License>> getActiveLicenses(@RequestParam Long userId) {
        return ResponseEntity.ok(licenseService.getActiveLicenses(userId));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License license) {
        return ResponseEntity.ok(licenseService.createLicense(license));
    }
}
