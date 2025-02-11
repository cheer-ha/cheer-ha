package com.project.cheerha.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter //setter 없으면 설정 안 돌아감
@ConfigurationProperties(prefix = "jwt")
public class JwtSecurityProperties {

    private Secret secret = new Secret();
    private Token token = new Token();

    @Getter
    @Setter
    public static class Secret {
        private String key;
    }

    @Getter
    @Setter
    public static class Token {
        private String prefix;
        private long expiration;
    }
}
