package ru.ispo.music_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistCreateDto {
    private String name;        // Название плейлиста (обязательное)
    private String description; // Описание плейлиста (опциональное)
}