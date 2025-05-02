package ru.ispo.music_service.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PlaylistCreateDto {
    private String name;        // Название плейлиста (обязательное)
    private String description; // Описание плейлиста (опциональное)
    private List<Integer> trackIds; // Список ID треков для добавления в плейлист
    private BigDecimal price;   // Цена плейлиста (опциональное)
}