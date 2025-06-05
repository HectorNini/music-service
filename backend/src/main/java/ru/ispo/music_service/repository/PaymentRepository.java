package ru.ispo.music_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import ru.ispo.music_service.entity.Payment;
import ru.ispo.music_service.entity.User;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByPaymentId(Integer paymentId); // Поиск платежа по уникальному идентификатору транзакции
    @EntityGraph(attributePaths = {"license", "license.user"})
    List<Payment> findByLicense_User(User user); // Поиск платежей по пользователю через лицензию
}
