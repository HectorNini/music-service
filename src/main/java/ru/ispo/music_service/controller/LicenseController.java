package ru.ispo.music_service.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.ispo.music_service.entity.License;
import ru.ispo.music_service.entity.User;
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
    public ResponseEntity<List<License>> getActiveLicenses(
            @AuthenticationPrincipal User user) { // Получаем авторизованного пользователя
        return ResponseEntity.ok(licenseService.getActiveLicenses(user.getUserId()));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(
            @RequestBody License license,
            @AuthenticationPrincipal User user) {
        license.setUser(user); // Устанавливаем владельца лицензии
        return ResponseEntity.ok(licenseService.createLicense(license));
    }
}
