package ru.ispo.music_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "\"Playlists\"")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Integer  playlistId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "playlist_price", nullable = false)
    private BigDecimal playlistPrice;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL)
    private List<PlaylistTrack> tracks;

}