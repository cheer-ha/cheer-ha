package com.project.cheerha.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public record BcryptSecurityProperties(Bcrypt bcrypt) {

    public record Bcrypt(int cost) {
    }
}