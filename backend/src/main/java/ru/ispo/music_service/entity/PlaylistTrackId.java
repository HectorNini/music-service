package ru.ispo.music_service.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
class PlaylistTrackId implements Serializable {
    private Integer  playlistId;
    private Integer  trackId;
}
