package com.project.cheerha.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private int career;

    @Column(length = 255, nullable = false )
    private String password;

    @Column(length = 5, nullable = false )
    private Role role;

    public static User of(String email, String name, int career, String password) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.career = career;
        user.password = password;
        user.role = Role.USER;
        return user;
    }
}
