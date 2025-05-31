package ru.ispo.music_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
public class PlaylistDto {
    private Integer playlistId; // Идентификатор плейлиста
    private String name;        // Название плейлиста
    private String description; // Описание плейлиста
    private List<TrackDto> tracks; // Добавлено поле для треков
    private Integer priceId; 
    private BigDecimal price; 
}
