package ru.ispo.music_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispo.music_service.entity.License;

import java.time.LocalDate;
import java.util.List;

public interface LicenseRepository extends JpaRepository<License, Long> {
    List<License> findByUserIdAndEndDateAfter(Long userId, LocalDate date);
}
