package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.repository.KeyValueCommandRepository;
import com.project.cheerha.common.repository.KeyValueQueryRepository;
import com.project.cheerha.common.util.SecureRandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class EmailTokenService {

    private final KeyValueQueryRepository keyValueQueryRepository;
    private final KeyValueCommandRepository keyValueCommandRepository;
    private final VerificationFailCount verificationFailCount;
    private final CheckDailyEmailCount checkDailyEmailCount;

    private static final String PASSWORD_TOKEN_PREFIX = "password_verification";
    private static final long TOKEN_EXPIRATION_MINUTES = 5;
    private static final long PASSWORD_TOKEN_EXPIRATION_MINUTES = 5;

    /**
     * 인증코드 생성 후 redis 에 저장
     */
    public String saveToken(String prefix, String email) {
        String token = generateRandomToken();
        String redisKey = prefix + ":" + email;
        keyValueCommandRepository.setValue(redisKey, token, TOKEN_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        checkDailyEmailCount.incrementDailyLimit(email, prefix);
        return token;
    }

    /**
     * 이메일 인증 코드 검증 후 해당 코드 무효화
     */
    public void verifyEmailToken(String prefix, String email, String token) {
        String redisKey = prefix + ":" + email;
        String storedToken = keyValueQueryRepository.getValue(redisKey);
        if(storedToken == null){
            throw new BadRequestException(ClientErrorCode.EMAIL_NOT_SENT_YET);
        }
        if (!storedToken.equals(token)) {
            verificationFailCount.incrementFailCount(email, prefix);
            throw new BadRequestException(ClientErrorCode.INVALID_EMAIL_VERIFICATION_TOKEN);
        }
        keyValueCommandRepository.removeValue(redisKey);
    }

    /**
     * 비밀번호 리셋용 토큰 생성
     */
    public String saveSecureToken(String email) {
        String redisKey = PASSWORD_TOKEN_PREFIX + ":" + email;
        String token = SecureRandomUtil.generateSecureToken();
        keyValueCommandRepository.setValue(redisKey, token, PASSWORD_TOKEN_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        return token;
    }

    /**
     * 패스워드 리셋용 토큰 검증, get 즉시 무효화(일회용)
     */
    public void verifyPasswordResetToken(String email, String token) {
        String redisKey = PASSWORD_TOKEN_PREFIX + ":" + email;
        String storedToken = keyValueQueryRepository.getValue(redisKey);
        keyValueCommandRepository.removeValue(redisKey);
        if (storedToken == null || !storedToken.equals(token)) {
            throw new BadRequestException(ClientErrorCode.INVALID_PASSWORD_RESET_TOKEN);
        }
    }

    /**
     * 이메일 인증코드 생성
     */
    private String generateRandomToken() {
        SecureRandom random = new SecureRandom();
        int token = 100000 + random.nextInt(900000);
        return String.valueOf(token);
    }
}
