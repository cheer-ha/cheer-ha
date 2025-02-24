package com.project.cheerha.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtSecurityProperties(
        Secret secret,
        Token token
) {
    public record Secret(
            String aesKey,
            String key
    ) {}

    public record Token(
            String prefix,
            String refreshPrefix,
            String blackListPrefix,
            long expiration,
            long refreshExpiration,
            long blackListExpiration
    ) {}
}
