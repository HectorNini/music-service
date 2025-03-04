package ru.ispo.music_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispo.music_service.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}