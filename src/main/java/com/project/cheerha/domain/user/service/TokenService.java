package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.util.SecureRandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final VerificationFailCount verificationFailCount;

    public static final String NOTIFICATION_VERIFICATION_TOKEN_PREFIX = "notification_email_verification_token";
    public static final String PASSWORD_VERIFICATION_TOKEN_PREFIX = "password_verification_token";
    private static final String PASSWORD_TOKEN_PREFIX = "password_verification";
    private static final long TOKEN_EXPIRATION_MINUTES = 5;
    private static final long PASSWORD_TOKEN_EXPIRATION_MINUTES = 5;

    /**
     * 이메일 알림 인증 코드 검증용 메서드
     * @param email 현재 사용자의 email
     * @param token 사용자의 이메일에 보낸 코드
     */
    public void verifyNotificationEmailToken(String email, String token) {
        String redisKey = getRedisKey(NOTIFICATION_VERIFICATION_TOKEN_PREFIX, email, token);
        redisTemplate.delete(redisKey);
    }

    /**
     * 패스워드 리셋 코드 검증용 메서드
     * @param email 비밀번호를 바꾸고자 하는 이메일
     * @param token 사용자의 이메일에 보낸 코드
     */
    public void verifyPasswordResetEmailToken(String email, String token) {
        String redisKey = getRedisKey(PASSWORD_VERIFICATION_TOKEN_PREFIX, email, token);
        redisTemplate.delete(redisKey);
    }

    /**
     * 인증코드를 redis 에 저장
     */
    public String saveToken(String notificationVerificationTokenPrefix, String email) {
        String token = generateRandomToken();
        String redisKey = notificationVerificationTokenPrefix + ":" + email;
        redisTemplate.opsForValue().set(redisKey, token, TOKEN_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        return token;
    }

    /**
     * 비밀번호 리셋용 토큰 생성
     */
    public String saveSecureToken(String email) {
        String redisKey = PASSWORD_TOKEN_PREFIX + ":" + email;
        String token = SecureRandomUtil.generateSecureToken();
        redisTemplate.opsForValue().set(redisKey, token, PASSWORD_TOKEN_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        return token;
    }

    /**
     * token 이 유효한지 확인
     */
    private String getRedisKey(String notificationVerificationTokenPrefix, String user, String token) {
        String redisKey = notificationVerificationTokenPrefix + ":" + user;
        String storedToken = redisTemplate.opsForValue().get(redisKey);
        if(storedToken == null){
            throw new BadRequestException(ClientErrorCode.EMAIL_NOT_SENT_YET);
        }
        if (!storedToken.equals(token)) {
            verificationFailCount.incrementFailCount(user, notificationVerificationTokenPrefix);
            throw new BadRequestException(ClientErrorCode.INVALID_EMAIL_VERIFICATION_TOKEN);
        }
        return redisKey;
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
