package ru.ispo.music_service.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ispo.music_service.dto.*;
import ru.ispo.music_service.entity.*;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        
        // Общие настройки
        mapper.getConfiguration()
            .setSkipNullEnabled(true)
            .setAmbiguityIgnored(true);
        
        // Конфигурация маппингов
        configureUserMapping(mapper);
        configureTrackMapping(mapper);
        configurePlaylistMapping(mapper);
        configureLicenseMapping(mapper);
        configurePaymentMapping(mapper);
        
        return mapper;
    }

    private void configureUserMapping(ModelMapper mapper) {
        // Явно создаем пустой TypeMap перед настройкой
        TypeMap<User, UserDto> typeMap = mapper.createTypeMap(User.class, UserDto.class);

        // Настраиваем маппинг вручную
        typeMap.addMappings(m -> {
            m.map(User::getUsername, UserDto::setUsername);
            m.map(User::getEmail, UserDto::setEmail);
            m.map(User::getFullName, UserDto::setFullName);
        });

    }

    private void configureTrackMapping(ModelMapper mapper) {
        TypeMap<Track, TrackDto> typeMap = mapper.createTypeMap(Track.class, TrackDto.class);
        typeMap.addMappings(m -> {
            m.map(Track::getTrackId, TrackDto::setTrackId);
            m.map(Track::getTitle, TrackDto::setTitle);
            m.map(Track::getArtist, TrackDto::setArtist);
            m.map(Track::getDuration, TrackDto::setDuration);
        });
    }

    private void configurePlaylistMapping(ModelMapper mapper) {
        TypeMap<Playlist, PlaylistDto> typeMap = mapper.createTypeMap(Playlist.class, PlaylistDto.class);
        typeMap.addMappings(m -> {
            m.map(Playlist::getPlaylistId, PlaylistDto::setPlaylistId);
            m.map(Playlist::getName, PlaylistDto::setName);
            m.map(Playlist::getDescription, PlaylistDto::setDescription);
        });
    }

    private void configureLicenseMapping(ModelMapper mapper) {
        TypeMap<License, LicenseDto> typeMap = mapper.createTypeMap(License.class, LicenseDto.class);
        typeMap.addMappings(m -> {
            m.map(License::getLicenseId, LicenseDto::setLicenseId);
            m.map(License::getEndDate, LicenseDto::setEndDate);
        });
    }

    private void configurePaymentMapping(ModelMapper mapper) {
        TypeMap<Payment, PaymentDto> typeMap = mapper.createTypeMap(Payment.class, PaymentDto.class);
        typeMap.addMappings(m -> {
            m.map(Payment::getPaymentId, PaymentDto::setPaymentId);
            m.map(Payment::getAmount, PaymentDto::setAmount);
            m.map(Payment::getStatus, PaymentDto::setStatus);
        });
    }
}