package ru.ispo.music_service.dto;



import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentCreateDto {
    private Integer userId;    // Идентификатор пользователя
    private Integer licenseId; // Идентификатор лицензии
    private BigDecimal amount; // Сумма платежа
    private String paymentMethod; // Метод оплаты
    private String transactionId; // Идентификатор транзакции
}