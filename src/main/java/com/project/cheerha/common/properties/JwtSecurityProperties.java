package com.project.cheerha.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtSecurityProperties {

    private Secret secret = new Secret();
    private Token token = new Token();

    @Getter
    @Setter
    public static class Secret {
        private String aesKey;
        private String key;
    }

    @Getter
    @Setter
    public static class Token {
        private String prefix;
        private String refreshPrefix;
        private String blackListPrefix;
        private long expiration;
        private long refreshExpiration;
        private long blackListExpiration;
    }
}
