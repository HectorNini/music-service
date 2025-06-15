package ru.ispo.music_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
public class PlaylistDto {
    private Integer playlistId; 
    private String name;       
    private String description;
    private List<TrackDto> tracks; 
    private Integer priceId; 
    private BigDecimal price; 
}
