package ru.ispo.music_service.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicenseCreateDto {
    private Integer priceId; // Ссылка на Pricing
    private LocalDate startDate;
    private LocalDate endDate;
}