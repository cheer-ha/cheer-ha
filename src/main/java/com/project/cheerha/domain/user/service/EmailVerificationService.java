package com.project.cheerha.domain.user.service;

import com.project.cheerha.domain.user.dto.request.SendEmailVerificationRequestDto;
import com.project.cheerha.domain.user.dto.response.ActivateNotificationResponseDto;
import com.project.cheerha.domain.user.dto.response.CreatePasswordResetTokenResponseDto;
import com.project.cheerha.domain.user.dto.response.SendEmailVerificationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private static final long CODE_EXPIRATION_MINUTES = 5;  // 인증 코드 유효 시간 (5분)

    public SendEmailVerificationResponseDto sendVerificationCode(SendEmailVerificationRequestDto requestDto) {
        String code = generateRandomCode();
        redisTemplate.opsForValue().set(requestDto.email(), code, CODE_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        sendEmail(requestDto.email(), code);
        return null;
    }

    public boolean verifyEmailCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get(email);
        return storedCode != null && storedCode.equals(code);
    }

    public ActivateNotificationResponseDto activateNotifications(String email) {
        return null;
    }

    //TODO: 다음 풀리퀘스트
    public CreatePasswordResetTokenResponseDto createPasswordResetToken(String email) {
        return null;
    }

    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999 사이의 6자리 숫자
        return String.valueOf(code);
    }

    //이메일 전송
    private void sendEmail(String email, String code) {

    }
}
