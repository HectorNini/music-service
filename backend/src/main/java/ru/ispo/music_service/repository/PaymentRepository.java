package ru.ispo.music_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispo.music_service.entity.Payment;
import ru.ispo.music_service.entity.User;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByPaymentId(Integer paymentId); // Поиск платежа по уникальному идентификатору транзакции
    List<Payment> findByUser(User user); // Поиск платежей по пользователю
}
