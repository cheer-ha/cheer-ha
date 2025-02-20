package com.project.cheerha.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "security")
public class BcryptSecurityProperties {

    private Bcrypt bcrypt = new Bcrypt();

    @Getter
    @Setter
    public static class Bcrypt {
        private int cost;
    }
}
