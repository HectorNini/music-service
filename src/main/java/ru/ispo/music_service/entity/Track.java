package ru.ispo.music_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tracks")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    private int duration; // В секундах

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "base_price", nullable = false)
    private double basePrice;

    // Getters, Setters
}