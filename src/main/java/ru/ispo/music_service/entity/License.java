package ru.ispo.music_service.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@Entity
@Table(name = "licenses")
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long licenseId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "price_paid", nullable = false)
    private double pricePaid;

    @Column(name = "purchased_at", nullable = false)
    private LocalDate purchasedAt = LocalDate.now();

    @AssertTrue(message = "License must have either a track or a playlist")
    private boolean isValid() {
        return (track != null) ^ (playlist != null);
    }

}