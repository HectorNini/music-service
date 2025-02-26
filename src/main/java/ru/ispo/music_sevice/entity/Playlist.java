package ru.ispo.music_sevice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistId;

    private String name;
    private String description;
    private double playlistPrice;

    @OneToMany(mappedBy = "playlist")
    private List<PlaylistTrack> tracks = new ArrayList<>();

    // Getters, Setters
}
