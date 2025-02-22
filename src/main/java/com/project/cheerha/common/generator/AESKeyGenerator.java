package com.project.cheerha.common.generator;

import java.security.SecureRandom;
import java.util.Base64;

//랜덤한 AES KEY 만드는 도구
public class AESKeyGenerator {
    public static void main(String[] args) {
        try {
            byte[] key = new byte[32];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(key);

            String encodedKey = Base64.getEncoder().encodeToString(key);
            System.out.println("생성된 AES 키: " + encodedKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}