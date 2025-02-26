package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.util.SecureRandomUtil;
import com.project.cheerha.domain.notice.service.EmailSender;
import com.project.cheerha.domain.user.dto.response.ActivateNotificationResponseDto;
import com.project.cheerha.domain.user.dto.response.CreatePasswordResetTokenResponseDto;
import com.project.cheerha.domain.user.dto.response.SendEmailVerificationResponseDto;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailSender emailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserFindByService userFindByService;

    private static final long CODE_EXPIRATION_MINUTES = 5;  // 인증 코드 유효 시간 (5분)
    private static final long PASSWORD_EXPIRATION_MINUTES = 5;
    private static final String NOTIFICATION_VERIFICATION_CODE_PREFIX = "notification_email_verification_code";
    private static final String PASSWORD_VERIFICATION_CODE_PREFIX = "password_verification_code";
    private static final String PASSWORD_TOKEN_PREFIX = "password_verification";
    private final UserRepository userRepository;

    public SendEmailVerificationResponseDto sendNotificationVerifyEmailVerificationCode(Long id) {
        User user = userFindByService.findById(id);
        if(user.isNotificationEnabled()){
            throw new BadRequestException(ClientErrorCode.ALREADY_VERIFIED_EMAIL);
        }
        String code = generateRandomCode();
        String redisKey = NOTIFICATION_VERIFICATION_CODE_PREFIX + ":"+  user.getEmail();
        redisTemplate.opsForValue().set(redisKey, code, CODE_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        emailSender.sendVerificationEmail(user.getEmail(), code);
        return SendEmailVerificationResponseDto.of();
    }

    public void verifyNotificationEmailCode(Long id, String code) {
        User user = userFindByService.findById(id);
        String redisKey = NOTIFICATION_VERIFICATION_CODE_PREFIX + ":" + user.getEmail();
        String storedCode = redisTemplate.opsForValue().get(redisKey);
        if(storedCode == null || !storedCode.equals(code)) {
            throw new BadRequestException(ClientErrorCode.INVALID_EMAIL_VERIFICATION_CODE);
        }
        redisTemplate.delete(redisKey);
    }

    public SendEmailVerificationResponseDto sendPasswordResetEmailVerificationCode(String email) {
        userRepository.existsByEmail(email);
        String code = generateRandomCode();
        String redisKey = PASSWORD_VERIFICATION_CODE_PREFIX + ":"+  email;
        redisTemplate.opsForValue().set(redisKey, code, CODE_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        emailSender.sendVerificationEmail(email, code);
        return SendEmailVerificationResponseDto.of();
    }

    public void verifyPasswordResetEmailCode(String email, String code) {
        String redisKey = PASSWORD_VERIFICATION_CODE_PREFIX + ":" + email;
        String storedCode = redisTemplate.opsForValue().get(redisKey);
        if(storedCode == null || !storedCode.equals(code)) {
            throw new BadRequestException(ClientErrorCode.INVALID_EMAIL_VERIFICATION_CODE);
        }
        redisTemplate.delete(redisKey);
    }

    @Transactional
    public ActivateNotificationResponseDto activateNotifications(Long id) {
        User user = userFindByService.findById(id);
        user.updateNotificationEnabled();
        return ActivateNotificationResponseDto.of();
    }

    public CreatePasswordResetTokenResponseDto createPasswordResetToken(String email) {
        User user = userFindByService.findByEmail(email);
        String redisKey = PASSWORD_TOKEN_PREFIX + ":" + user.getEmail();
        String token = SecureRandomUtil.generateSecureToken();
        redisTemplate.opsForValue().set(redisKey, token, PASSWORD_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        return CreatePasswordResetTokenResponseDto.of(token);
    }

    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
