package ru.ispo.music_service.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackDto {
    private Integer trackId;
    private String title;
    private String artist;

}