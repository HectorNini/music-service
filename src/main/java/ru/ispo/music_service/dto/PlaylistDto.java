package ru.ispo.music_service.dto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "playlistId")
public class PlaylistDto {
    private Integer playlistId;
    private String title;
    private String description;
    private UserDto owner;

}
