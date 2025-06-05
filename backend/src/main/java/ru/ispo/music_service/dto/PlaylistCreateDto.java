package ru.ispo.music_service.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PlaylistCreateDto {
    private String name;      
    private String description; 
    private List<Integer> trackIds; 
    private BigDecimal price;  
}