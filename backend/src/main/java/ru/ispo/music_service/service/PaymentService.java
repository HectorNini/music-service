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
import ru.ispo.music_service.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface PaymentService {
    PaymentDto createPayment(Integer licenseId, BigDecimal amount);
    List<PaymentDto> getPaymentsByUser(User user);
    List<PaymentDto> getPaymentsByUsername(String username);
    List<PaymentDto> getAllPayments();
    PaymentDto convertToDto(Payment payment);
}

@Service
class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final LicenseRepository licenseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                          LicenseRepository licenseRepository,
                          UserRepository userRepository,
                          ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.licenseRepository = licenseRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public PaymentDto createPayment(Integer licenseId, BigDecimal amount) {
        License license = licenseRepository.findByLicenseId(licenseId)
                .orElseThrow(() -> new EntityNotFoundException("License not found"));

        BigDecimal price = license.getPricing().getPrice(); // Берем цену из Pricing

        Payment payment = new Payment();
        payment.setLicense(license);// Устанавливаем пользователя из лицензии
        payment.setAmount(amount); // Используем переданную сумму
        payment.setStatus("COMPLETED");
        payment.setPaymentMethod("Credit Card");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId(UUID.randomUUID().toString());

        Payment savedPayment = paymentRepository.save(payment);
        return convertToDto(savedPayment);
    }

    @Override
    public List<PaymentDto> getPaymentsByUser(User user) {
        List<Payment> payments = paymentRepository.findByLicense_User(user);
        return payments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getPaymentsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return getPaymentsByUser(user);
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDto convertToDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(payment.getPaymentId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setStatus(payment.getStatus());
        dto.setPaymentMethod(payment.getPaymentMethod());

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