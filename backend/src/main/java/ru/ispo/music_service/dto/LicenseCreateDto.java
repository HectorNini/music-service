package ru.ispo.music_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
public class LicenseCreateDto {
    private Integer trackId;
    private Integer playlistId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal pricePaid;
    // Геттеры и сеттеры
}