package ru.ispo.music_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MusicServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MusicServiceApplication.class, args);
	}

}
