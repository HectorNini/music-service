package ru.ispo.music_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@Entity
@Table(name = "\"Tracks\"")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private Integer  trackId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    private int duration; // В секундах

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "base_price", nullable = false)
    private BigInteger basePrice;

    // Getters, Setters
}