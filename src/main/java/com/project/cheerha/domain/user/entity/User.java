package com.project.cheerha.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "`user`")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private int career;

    @Column(nullable = false)
    private int age;

    @Column(length = 255, nullable = false )
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 5, nullable = false )
    private Role role;

    private boolean isNotificationEnabled = false;

    public static User toEntity(String email, String name, int age, int career, String password) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.career = career;
        user.password = password;
        user.age = age;
        user.role = Role.USER;
        return user;
    }

    public void updateNotificationEnabled() {
        this.isNotificationEnabled = true;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
