package com.project.cheerha.common.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.project.cheerha.common.properties.BcryptSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoder {

    private final BcryptSecurityProperties securityProperties;
    private final int cost = securityProperties.getBcrypt().getCost();

    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(cost, rawPassword.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}
