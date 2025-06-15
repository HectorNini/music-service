package ru.ispo.music_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {
    private Integer paymentId; // Идентификатор платежа
    private BigDecimal amount; // Сумма платежа
    private LocalDateTime paymentDate; // Дата платежа
    private String productDescription; 
    private String status;
    private String paymentMethod;
}