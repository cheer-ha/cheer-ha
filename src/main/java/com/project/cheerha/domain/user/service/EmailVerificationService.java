package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.domain.user.dto.response.ActivateNotificationResponseDto;
import com.project.cheerha.domain.user.dto.response.CreatePasswordResetTokenResponseDto;
import com.project.cheerha.domain.user.dto.response.SendEmailVerificationResponseDto;
import com.project.cheerha.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserFindByService userFindByService;

    private static final long CODE_EXPIRATION_MINUTES = 5;  // 인증 코드 유효 시간 (5분)
    private static final String VERIFICATION_CODE_PREFIX = "email_verification";

    public SendEmailVerificationResponseDto sendVerificationCode(Long id) {
        User user = userFindByService.findById(id);
        if(user.isNotificationEnabled()){
            throw new BadRequestException(ClientErrorCode.ALREADY_VERIFIED_EMAIL);
        }
        String code = generateRandomCode();
        String redisKey = VERIFICATION_CODE_PREFIX + ":"+  user.getEmail();
        redisTemplate.opsForValue().set(redisKey, code, CODE_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        sendEmail(user.getEmail(), code);
        return SendEmailVerificationResponseDto.of();
    }

    public void verifyEmailCode(String email, String code) {
        String redisKey = VERIFICATION_CODE_PREFIX + ":" + email;
        String storedCode = redisTemplate.opsForValue().get(redisKey);
        if(storedCode == null || !storedCode.equals(code)) {
            throw new BadRequestException(ClientErrorCode.INVALID_EMAIL_VERIFICATION_CODE);
        }
    }

    @Transactional
    public ActivateNotificationResponseDto activateNotifications(String email) {
        User user = userFindByService.findByEmail(email);
        user.updateNotificationEnabled();
        return ActivateNotificationResponseDto.of();
    }

    //TODO: 다음 풀리퀘스트
    public CreatePasswordResetTokenResponseDto createPasswordResetToken(String email) {
        return null;
    }

    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    //TODO: email 지현님 로직으로 구현
    private void sendEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증 코드");
        message.setText("인증 코드: " + code);
        javaMailSender.send(message);
    }
}
