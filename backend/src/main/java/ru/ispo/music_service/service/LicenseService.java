package ru.ispo.music_service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.ispo.music_service.dto.LicenseCreateDto;
import ru.ispo.music_service.dto.LicenseDto;
import ru.ispo.music_service.entity.License;
import ru.ispo.music_service.entity.Pricing;
import ru.ispo.music_service.entity.User;
import ru.ispo.music_service.repository.LicenseRepository;
import ru.ispo.music_service.repository.PricingRepository;
import ru.ispo.music_service.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LicenseService {
    private final LicenseRepository licenseRepository;
    private final PricingRepository pricingRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public LicenseService(LicenseRepository licenseRepository,
                          PricingRepository pricingRepository,
                          ModelMapper modelMapper,
                          UserRepository userRepository) {
        this.licenseRepository = licenseRepository;
        this.pricingRepository = pricingRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public List<LicenseDto> getActiveLicenses(Integer userId) {
        List<License> licenses = licenseRepository.findByUser_UserIdAndEndDateAfter(userId, LocalDate.now());
        return licenses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public LicenseDto buyLicense(Integer priceId, User user) {
        Pricing pricing = pricingRepository.findById(priceId)
                .orElseThrow(() -> new EntityNotFoundException("Цена не найдена"));

        // Проверяем, что цена активна
        if (pricing.getValidTo().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Срок действия цены истек");
        }

        License license = new License();
        license.setUser(user);
        license.setPricing(pricing);
        license.setStartDate(LocalDate.now());
        license.setEndDate(LocalDate.now().plusYears(1)); // Пример: подписка на год

        License savedLicense = licenseRepository.save(license);
        return convertToDto(savedLicense);
    }

    public LicenseDto createLicense(LicenseCreateDto dto, User user) {
        License license = modelMapper.map(dto, License.class);
        license.setUser(user);

        // Находим Pricing по priceId из DTO
        Pricing pricing = pricingRepository.findById(dto.getPriceId())
                .orElseThrow(() -> new EntityNotFoundException("Pricing not found"));
        license.setPricing(pricing);

        License savedLicense = licenseRepository.save(license);
        return convertToDto(savedLicense);
    }

    public LicenseDto convertToDto(License license) {
        LicenseDto dto = new LicenseDto();
        dto.setLicenseId(license.getLicenseId());
        dto.setEndDate(license.getEndDate());

        // Получаем название продукта через Pricing
        if (license.getPricing().getTrack() != null) {
            dto.setProductName(license.getPricing().getTrack().getTitle());
        } else if (license.getPricing().getPlaylist() != null) {
            dto.setProductName(license.getPricing().getPlaylist().getName());
        }

        return dto;
    }
}