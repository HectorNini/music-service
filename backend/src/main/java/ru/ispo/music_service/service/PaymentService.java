package ru.ispo.music_service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.ispo.music_service.dto.PaymentDto;
import ru.ispo.music_service.entity.License;
import ru.ispo.music_service.entity.Payment;
import ru.ispo.music_service.entity.User;
import ru.ispo.music_service.repository.LicenseRepository;
import ru.ispo.music_service.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final LicenseRepository licenseRepository;
    private final ModelMapper modelMapper;

    public PaymentService(PaymentRepository paymentRepository,
                          LicenseRepository licenseRepository, ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.licenseRepository = licenseRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public PaymentDto createPayment(Integer licenseId) {
        License license = licenseRepository.findByLicenseId(licenseId)
                .orElseThrow(() -> new EntityNotFoundException("License not found"));

        BigDecimal price = license.getPricing().getPrice(); // Берем цену из Pricing

        Payment payment = new Payment();
        payment.setLicense(license);
        payment.setUser(license.getUser()); // Устанавливаем пользователя из лицензии
        payment.setAmount(price);
        payment.setStatus("COMPLETED");
        payment.setPaymentMethod("Credit Card");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId(UUID.randomUUID().toString());

        Payment savedPayment = paymentRepository.save(payment);
        return convertToDto(savedPayment);
    }

    // Получить все платежи пользователя
    public List<PaymentDto> getPaymentsByUser(User user) {
        List<Payment> payments = paymentRepository.findByUser(user);
        return payments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Получить все платежи (только для админов)
    public List<PaymentDto> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PaymentDto convertToDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(payment.getPaymentId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPaymentDate());

        // Получаем описание продукта из лицензии
        License license = payment.getLicense();
        if (license.getPricing().getTrack() != null) {
            dto.setProductDescription("Трек: " + license.getPricing().getTrack().getTitle());
        } else if (license.getPricing().getPlaylist() != null) {
            dto.setProductDescription("Плейлист: " + license.getPricing().getPlaylist().getName());
        }

        return dto;
    }
}