package ru.ispo.music_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/error", "/css/**", "/js/**").permitAll() // Разрешить доступ без аутентификации
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Страница логина
                        .loginProcessingUrl("/login") // URL для обработки формы
                        .defaultSuccessUrl("/home", true) // Перенаправление после успешного входа
                        .failureUrl("/login?error=true") // Перенаправление при ошибке
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }
}