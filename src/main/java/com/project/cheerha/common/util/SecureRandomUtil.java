package com.project.cheerha.common.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SecureRandomUtil {

    public static String generateSecureToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
