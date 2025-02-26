package ru.ispo.music_sevice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispo.music_sevice.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}