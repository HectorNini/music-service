package ru.ispo.music_sevice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispo.music_sevice.entity.License;

import java.time.LocalDate;
import java.util.List;

public interface LicenseRepository extends JpaRepository<License, Long> {
    List<License> findByUserIdAndEndDateAfter(Long userId, LocalDate date);
}
