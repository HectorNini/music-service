package ru.ispo.music_service.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseDto {
    private Integer licenseId;
    private LocalDate endDate;
    private String productName;
    private List<TrackDto> tracks;
}