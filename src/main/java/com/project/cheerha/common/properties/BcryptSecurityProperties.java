package com.project.cheerha.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter //setter 없으면 설정 안 돌아감
@Configuration
@ConfigurationProperties(prefix = "security")
public class BcryptSecurityProperties {

    private Bcrypt bcrypt = new Bcrypt();

    @Getter
    @Setter
    public static class Bcrypt {
        private String cost;
    }
}
