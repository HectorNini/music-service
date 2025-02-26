package ru.ispo.music_service.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long licenseId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    private LocalDate startDate;
    private LocalDate endDate;
    private double pricePaid;
    private boolean isOwnerLicense;

    // Проверка: трек ИЛИ плейлист
    @AssertTrue(message = "License must have either track or playlist")
    private boolean isValid() {
        return (track != null) ^ (playlist != null);
    }

    // Getters, Setters
}