package ru.ispo.music_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicenseDto {
    private Integer licenseId;
    private UserDto user;
    private TrackDto track;
    private PlaylistDto playlist;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal pricePaid;
    private LocalDate purchasedAt;

    // Конструкторы, геттеры и сеттеры
}
