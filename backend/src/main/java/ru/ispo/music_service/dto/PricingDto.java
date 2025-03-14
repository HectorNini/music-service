package ru.ispo.music_service.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PricingDto {
    private Integer priceId;
    private Integer trackId;
    private Integer playlistId;
    private BigDecimal price;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
}
