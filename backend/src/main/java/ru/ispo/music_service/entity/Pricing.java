package ru.ispo.music_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "\"Pricing\"")
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Integer priceId;

    @ManyToOne
    @JoinColumn(name = "track_id") //либо цена на трек
    private Track track;

    @ManyToOne
    @JoinColumn(name = "playlist_id") //либо цена на плейлис
    private Playlist playlist;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, name = "valid_from")
    private LocalDateTime validFrom;

    @Column(nullable = false, name = "valid_to")
    private LocalDateTime validTo;
}