package ru.ispo.music_service.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ispo.music_service.dto.UserDto;
import ru.ispo.music_service.entity.User;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        configureUserMapping(mapper);
        return mapper;
    }

    private void configureUserMapping(ModelMapper mapper) {
        // Явно создаем пустой TypeMap перед настройкой
        TypeMap<User, UserDto> typeMap = mapper.createTypeMap(User.class, UserDto.class);

        // Настраиваем маппинг вручную
        typeMap.addMappings(m -> {
            m.map(User::getUserId, UserDto::setUserId);
            m.map(User::getUsername, UserDto::setUsername);
            m.map(User::getEmail, UserDto::setEmail);
            m.map(User::getFullName, UserDto::setFullName);
            m.<String>map(src -> src.getRole().getName(), UserDto::setRoleName);
        });

    }
}