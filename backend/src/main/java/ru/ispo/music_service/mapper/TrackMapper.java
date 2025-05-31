package ru.ispo.music_service.mapper;

import org.springframework.stereotype.Component;
import ru.ispo.music_service.dto.TrackDto;
import ru.ispo.music_service.entity.Track;
import ru.ispo.music_service.repository.PricingRepository;

@Component
public class TrackMapper {
    private final PricingRepository pricingRepository;

    public TrackMapper(PricingRepository pricingRepository) {
        this.pricingRepository = pricingRepository;
    }

    public TrackDto toDto(Track track) {
        TrackDto dto = new TrackDto();
        dto.setTrackId(track.getTrackId());
        dto.setTitle(track.getTitle());
        dto.setArtist(track.getArtist());
        dto.setDuration(track.getDuration());
        dto.setFilePath(track.getFilePath());

        // Добавляем информацию о цене трека
        pricingRepository.findActiveByTrackId(track.getTrackId())
                .ifPresent(pricing -> {
                    dto.setPriceId(pricing.getPriceId());
                    dto.setPrice(pricing.getPrice());
                });

        return dto;
    }

    public Track toEntity(TrackDto dto) {
        Track track = new Track();
        track.setTrackId(dto.getTrackId());
        track.setTitle(dto.getTitle());
        track.setArtist(dto.getArtist());
        track.setDuration(dto.getDuration());
        track.setFilePath(dto.getFilePath());
        return track;
    }
} 