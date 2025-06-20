package ru.ispo.music_service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.ispo.music_service.dto.LicenseCreateDto;
import ru.ispo.music_service.dto.LicenseDto;
import ru.ispo.music_service.dto.TrackDto;
import ru.ispo.music_service.entity.License;
import ru.ispo.music_service.entity.Pricing;
import ru.ispo.music_service.entity.Track;
import ru.ispo.music_service.entity.User;
import ru.ispo.music_service.repository.LicenseRepository;
import ru.ispo.music_service.repository.PlaylistTrackRepository;
import ru.ispo.music_service.repository.PricingRepository;
import ru.ispo.music_service.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public interface LicenseService {
    List<LicenseDto> getActiveLicenses(Integer userId);
    LicenseDto buyLicense(Integer priceId, Integer months, User user);
    LicenseDto createLicense(LicenseCreateDto dto, User user);
    List<LicenseDto> getAllLicenses();
}

@Service
class LicenseServiceImpl implements LicenseService {
    private final LicenseRepository licenseRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final PricingRepository pricingRepository;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;

    public LicenseServiceImpl(LicenseRepository licenseRepository,
                          PlaylistTrackRepository playlistTrackRepository,
                          PricingRepository pricingRepository,
                          ModelMapper modelMapper,
                          UserRepository userRepository,
                          PaymentService paymentService) {
        this.licenseRepository = licenseRepository;
        this.playlistTrackRepository = playlistTrackRepository;
        this.pricingRepository = pricingRepository;
        this.modelMapper = modelMapper;
        this.paymentService = paymentService;
    }

    @Override
    public List<LicenseDto> getActiveLicenses(Integer userId) {
        List<License> licenses = licenseRepository.findByUser_UserIdAndEndDateAfter(userId, LocalDate.now());
        return licenses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LicenseDto buyLicense(Integer priceId, Integer months, User user) {
        Pricing pricing = pricingRepository.findById(priceId)
                .orElseThrow(() -> new EntityNotFoundException("Цена не найдена"));

        // Проверяем, что цена активна
        if (pricing.getValidTo().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Срок действия цены истек");
        }

        // Рассчитываем скидку в зависимости от срока
        BigDecimal discountRate = calculateDiscountRate(months);
        BigDecimal totalAmountPaid = pricing.getPrice() // Используем базовую цену из Pricing
                .multiply(BigDecimal.valueOf(months))
                .multiply(discountRate);

        License license = new License();
        license.setUser(user);
        license.setPricing(pricing); // Связываем с оригинальной записью Pricing
        license.setStartDate(LocalDate.now());
        license.setEndDate(LocalDate.now().plusMonths(months));

        License savedLicense = licenseRepository.save(license);

        // Создаем платеж с рассчитанной полной суммой
        paymentService.createPayment(savedLicense.getLicenseId(), totalAmountPaid);

        return convertToDto(savedLicense);
    }

    private BigDecimal calculateDiscountRate(Integer months) {
        return switch (months) {
            case 1 -> BigDecimal.ONE; // Базовая цена
            case 3 -> new BigDecimal("0.9"); // 10% скидка
            case 6 -> new BigDecimal("0.85"); // 15% скидка
            case 12 -> new BigDecimal("0.8"); // 20% скидка
            default -> throw new IllegalArgumentException("Неподдерживаемый срок лицензии");
        };
    }

    @Override
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

    @Override
    public List<LicenseDto> getAllLicenses() {
        List<License> licenses = licenseRepository.findAll();
        return licenses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public LicenseDto convertToDto(License license) {
        LicenseDto dto = new LicenseDto();
        dto.setLicenseId(license.getLicenseId());
        dto.setEndDate(license.getEndDate());

        // Получаем название продукта и треки через Pricing
        if (license.getPricing().getTrack() != null) {
            Track track = license.getPricing().getTrack();
            dto.setProductName(track.getTitle());
            // Для одиночного трека создаем список из одного элемента
            TrackDto trackDto = new TrackDto();
            trackDto.setTrackId(track.getTrackId());
            trackDto.setTitle(track.getTitle());
            trackDto.setArtist(track.getArtist());
            trackDto.setDuration(track.getDuration());
            dto.setTracks(List.of(trackDto));
        } else if (license.getPricing().getPlaylist() != null) {
            dto.setProductName(license.getPricing().getPlaylist().getName());
            // Получаем все треки из плейлиста
            List<Track> tracks = playlistTrackRepository.findTracksByPlaylistId(
                license.getPricing().getPlaylist().getPlaylistId()
            );
            dto.setTracks(tracks.stream()
                .map(track -> {
                    TrackDto trackDto = new TrackDto();
                    trackDto.setTrackId(track.getTrackId());
                    trackDto.setTitle(track.getTitle());
                    trackDto.setArtist(track.getArtist());
                    trackDto.setDuration(track.getDuration());
                    return trackDto;
                })
                .collect(Collectors.toList()));
        }

        return dto;
    }
}