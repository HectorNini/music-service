package ru.ispo.music_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String roleName; // Только название роли вместо объекта

    // Геттеры и сеттеры
}