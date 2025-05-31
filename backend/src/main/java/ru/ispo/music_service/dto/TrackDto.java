package ru.ispo.music_service.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TrackDto {
    private Integer trackId;
    private String title;
    private String artist;
    private Integer duration;
    private String filePath;
    private Integer priceId;
    private BigDecimal price;
}