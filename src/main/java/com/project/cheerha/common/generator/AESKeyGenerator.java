package com.project.cheerha.common.generator;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;

//랜덤한 AES KEY 만드는 도구
@Slf4j
public class AESKeyGenerator {
    public static void main(String[] args) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256, new SecureRandom());
            SecretKey secretKey = keyGen.generateKey();

            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            log.info("생성된 AES 키: {}", encodedKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}