package ru.ispo.music_service.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.ispo.music_service.entity.Role;
import ru.ispo.music_service.repository.RoleRepository;

@Component
public class RoleInitializer {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        createRoleIfNotFound("admin");
        createRoleIfNotFound("user");
        createRoleIfNotFound("owner");
    }

    private void createRoleIfNotFound(String name) {
        if (!roleRepository.existsByNameIgnoreCase(name)) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }
}
