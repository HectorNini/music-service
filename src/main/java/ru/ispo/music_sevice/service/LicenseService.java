package ru.ispo.music_sevice.service;


import org.springframework.stereotype.Service;
import ru.ispo.music_sevice.entity.License;
import ru.ispo.music_sevice.repository.LicenseRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class LicenseService {
    private final LicenseRepository licenseRepository;

    public LicenseService(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    public List<License> getActiveLicenses(Long userId) {
        return licenseRepository.findByUserIdAndEndDateAfter(userId, LocalDate.now());
    }

    public License createLicense(License license) {
        if (license.getTrack() == null && license.getPlaylist() == null) {
            throw new IllegalArgumentException("License must have track or playlist");
        }
        return licenseRepository.save(license);
    }
}