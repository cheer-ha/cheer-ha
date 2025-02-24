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
@Table(name = "banned_ip")
public class BannedIp {

    @Id
    private String ip;

    private String message;

    private ZonedDateTime createdAt = ZonedDateTime.now();

    private boolean isDeleted = false;

    public static BannedIp toEntity(String ip, String message) {
        BannedIp bannedIp = new BannedIp();
        bannedIp.ip = ip;
        bannedIp.message = message;
        return bannedIp;
    }
}
