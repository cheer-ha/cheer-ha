package com.project.cheerha.domain.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "banned_email")
public class BannedEmail {

    @Id
    private String email;

    private String message;

    private ZonedDateTime createdAt = ZonedDateTime.now();

    public static BannedEmail toEntity(String email, String message) {
        BannedEmail bannedIp = new BannedEmail();
        bannedIp.email = email;
        bannedIp.message = message;
        return bannedIp;
    }
}
