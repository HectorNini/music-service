package ru.ispo.music_service.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.ispo.music_service.dto.LicenseCreateDto;
import ru.ispo.music_service.dto.LicenseDto;
import ru.ispo.music_service.dto.PaymentDto;
import ru.ispo.music_service.entity.License;
import ru.ispo.music_service.entity.User;
import ru.ispo.music_service.repository.UserRepository;
import ru.ispo.music_service.service.LicenseService;
import ru.ispo.music_service.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/licenses")
public class LicenseController {
    private final LicenseService licenseService;
    private final PaymentService paymentService;
    private final UserRepository userRepository;

    public LicenseController(LicenseService licenseService, PaymentService paymentService, UserRepository userRepository) {
        this.licenseService = licenseService;
        this.paymentService = paymentService;
        this.userRepository = userRepository;
    }

    @PostMapping("/buy")
    public ResponseEntity<LicenseDto> buyLicense(
            @RequestParam("priceId") Integer priceId,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Создаем лицензию
        LicenseDto license = licenseService.buyLicense(priceId, user);

        // Создаем платеж для этой лицензии
        PaymentDto payment = paymentService.createPayment(license.getLicenseId());

        // Возвращаем DTO лицензии
        return ResponseEntity.ok(license);
    }


    @GetMapping("/active")
    public ResponseEntity<List<LicenseDto>> getActiveLicenses(
            @AuthenticationPrincipal UserDetails userDetails) { // Используем UserDetails
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(licenseService.getActiveLicenses(user.getUserId()));
    }

    @PostMapping
    public ResponseEntity<LicenseDto> createLicense(
            @RequestBody @Valid LicenseCreateDto licenseCreateDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        LicenseDto license = licenseService.createLicense(licenseCreateDto, user);
        return ResponseEntity.ok(license);
    }
}