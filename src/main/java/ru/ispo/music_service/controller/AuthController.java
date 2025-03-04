package ru.ispo.music_service.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ispo.music_service.entity.Role;
import ru.ispo.music_service.entity.User;
import ru.ispo.music_service.repository.RoleRepository;
import ru.ispo.music_service.repository.UserRepository;

import java.time.ZonedDateTime;

@RestController
public class AuthController {

//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public AuthController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @PostMapping("/register")
//    public String registerUser(
//            @RequestParam String username,
//            @RequestParam String password,
//            @RequestParam String email,
//            @RequestParam String fullName,
//            @RequestParam String roleName
//    ) {
//        User user = new User();
//        user.setUsername(username);
//        user.setPasswordHash(passwordEncoder.encode(password));
//        user.setEmail(email);
//        user.setFullName(fullName);
//        user.setCreatedAt(ZonedDateTime.now());
//        user.setLastLogin(ZonedDateTime.now());
//
//        Role role = roleRepository.findByName(roleName)
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//        user.setRole(role);
//
//        userRepository.save(user);
//        return "User registered!";
//    }
@GetMapping("/login")
public String login() {
    return "login";
}
}