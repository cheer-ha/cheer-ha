package com.project.cheerha.common.util;

import com.project.cheerha.common.exception.auth.AuthErrorCode;
import com.project.cheerha.common.exception.auth.UnAuthorizedException;
import com.project.cheerha.common.exception.server.EncryptException;
import com.project.cheerha.common.exception.server.ServerErrorCode;
import com.project.cheerha.common.properties.JwtSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class Aes256Util {

    private final JwtSecurityProperties jwtSecurityProperties;

    public String encrypt(String plainText) {
        try {
            String aesSecretKey = jwtSecurityProperties.getSecret().getAesKey();
            SecretKeySpec secretKey = new SecretKeySpec(aesSecretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new EncryptException(ServerErrorCode.TOKEN_ENCRYPTION_FAILED);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            String aesSecretKey = jwtSecurityProperties.getSecret().getAesKey();
            SecretKeySpec secretKey = new SecretKeySpec(aesSecretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new UnAuthorizedException(AuthErrorCode.TOKEN_DECRYPTION_FAILED);
        }
    }
}
