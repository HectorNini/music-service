package ru.ispo.music_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.ispo.music_service.dto.LicenseCreateDto;
import ru.ispo.music_service.dto.LicenseDto;
import ru.ispo.music_service.dto.PaymentDto;
import ru.ispo.music_service.entity.User;
import ru.ispo.music_service.service.LicenseService;
import ru.ispo.music_service.service.PaymentService;
import ru.ispo.music_service.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/licenses")
@RequiredArgsConstructor
public class LicenseController {
    private final LicenseService licenseService;
    private final PaymentService paymentService;
    private final UserService userService;

    @PostMapping("/buy")
    public ResponseEntity<LicenseDto> buyLicense(
            @RequestParam("priceId") Integer priceId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        LicenseDto license = licenseService.buyLicense(priceId, user);
        PaymentDto payment = paymentService.createPayment(license.getLicenseId());
        return ResponseEntity.ok(license);
    }

    @GetMapping("/active")
    public ResponseEntity<List<LicenseDto>> getActiveLicenses(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(licenseService.getActiveLicenses(user.getUserId()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LicenseDto> createLicense(
            @RequestBody @Valid LicenseCreateDto licenseCreateDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(licenseService.createLicense(licenseCreateDto, user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LicenseDto>> getAllLicenses() {
        return ResponseEntity.ok(licenseService.getAllLicenses());
    }
}