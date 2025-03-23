package ru.ispo.music_service.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "\"Roles\"")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer  roleId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Role(int i, String user) {
        roleId = i;
        name = user;
    }
}