package ru.ispo.music_service.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TrackDto {
    private Integer trackId;
    private String title;
    private String artist;
    private Integer priceId; // Новое поле
    private BigDecimal price; // Новое поле

}