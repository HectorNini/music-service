package ru.ispo.music_sevice.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PlaylistTrackId implements Serializable {
    private Long playlistId;
    private Long trackId;
}
