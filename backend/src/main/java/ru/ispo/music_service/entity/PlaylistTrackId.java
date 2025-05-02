package ru.ispo.music_service.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class PlaylistTrackId implements Serializable {
    private Integer playlistId;
    private Integer trackId;
}
