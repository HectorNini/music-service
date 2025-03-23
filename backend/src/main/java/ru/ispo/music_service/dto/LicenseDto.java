package ru.ispo.music_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;


import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseDto {
    private Integer licenseId;
    private LocalDate endDate;
    private String productName;
}