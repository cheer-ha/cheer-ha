package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.common.util.SecureRandomUtil;
import com.project.cheerha.domain.notice.service.EmailSender;
import com.project.cheerha.domain.user.dto.response.ActivateNotificationResponseDto;
import com.project.cheerha.domain.user.dto.response.VerifyPasswordResetCodeResponseDto;
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
public class UserEmailVerificationService {

    private final EmailSender emailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserFindByService userFindByService;
    private final UserRepository userRepository;
    private final CheckDailyEmailCount checkDailyEmailCount;
    private final VerificationFailCount verificationFailCount;

    private static final long CODE_EXPIRATION_MINUTES = 5;
    private static final long PASSWORD_TOKEN_EXPIRATION_MINUTES = 5;
    private static final String NOTIFICATION_VERIFICATION_CODE_PREFIX = "notification_email_verification_code";
    private static final String PASSWORD_VERIFICATION_CODE_PREFIX = "password_verification_code";
    private static final String PASSWORD_TOKEN_PREFIX = "password_verification";

    /**
     * 이메일 알림을 받기 위해 이메일인증을 보내는 메서드
     * @param id 현재 사용자의 id
     */
    public SendEmailVerificationResponseDto sendNotificationVerifyEmailVerificationCode(Long id) {
        User user = userFindByService.findById(id);
        if(user.isNotificationEnabled()){
            throw new BadRequestException(ClientErrorCode.ALREADY_VERIFIED_EMAIL);
        }
        checkDailyEmailCount.incrementDailyLimit(user.getEmail(), NOTIFICATION_VERIFICATION_CODE_PREFIX);
        String code = generateRandomCode();
        String redisKey = NOTIFICATION_VERIFICATION_CODE_PREFIX + ":"+  user.getEmail();
        redisTemplate.opsForValue().set(redisKey, code, CODE_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        emailSender.sendVerificationEmail(user.getEmail(), code);
        return SendEmailVerificationResponseDto.of();
    }

    /**
     * 이메일 알림 인증 코드 검증용 메서드
     * @param id 현재 사용자의 id
     * @param code 사용자의 이메일에 보낸 코드
     */
    public void verifyNotificationEmailCode(Long id, String code) {
        User user = userFindByService.findById(id);
        String redisKey = NOTIFICATION_VERIFICATION_CODE_PREFIX + ":" + user.getEmail();
        String storedCode = redisTemplate.opsForValue().get(redisKey);
        if(storedCode == null || !storedCode.equals(code)) {
            verificationFailCount.incrementFailCount(user.getEmail(), NOTIFICATION_VERIFICATION_CODE_PREFIX);
            throw new BadRequestException(ClientErrorCode.INVALID_EMAIL_VERIFICATION_CODE);
        }
        redisTemplate.delete(redisKey);
    }

    /**
     * 이메일 알림 허용유무를 업데이트하는 메서드
     * @param id 현재사용자의 id
     */
    @Transactional
    public ActivateNotificationResponseDto activateNotification(Long id) {
        User user = userFindByService.findById(id);
        user.updateNotificationEnabled();
        return ActivateNotificationResponseDto.of();
    }

    /**
     * 비로그인 사용자의 패스워드 리셋을 위해 이메일인증을 보내는 메서드
     * @param email 비밀번호를 바꾸고자 하는 이메일
     */
    public SendEmailVerificationResponseDto sendPasswordResetEmailVerificationCode(String email) {
        if(!userRepository.existsByEmail(email)){
            throw new NotFoundException(DataErrorCode.USER_NOT_FOUND);
        }
        checkDailyEmailCount.incrementDailyLimit(email, PASSWORD_VERIFICATION_CODE_PREFIX);
        String code = generateRandomCode();
        String redisKey = PASSWORD_VERIFICATION_CODE_PREFIX + ":"+  email;
        redisTemplate.opsForValue().set(redisKey, code, CODE_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        emailSender.sendVerificationEmail(email, code);
        return SendEmailVerificationResponseDto.of();
    }

    /**
     * 패스워드 리셋 코드 검증용 메서드
     * @param email 비밀번호를 바꾸고자 하는 이메일
     * @param code 사용자의 이메일에 보낸 코드
     */
    public void verifyPasswordResetEmailCode(String email, String code) {
        String redisKey = PASSWORD_VERIFICATION_CODE_PREFIX + ":" + email;
        String storedCode = redisTemplate.opsForValue().get(redisKey);
        if(storedCode == null || !storedCode.equals(code)) {
            verificationFailCount.incrementFailCount(email, PASSWORD_VERIFICATION_CODE_PREFIX);
            throw new BadRequestException(ClientErrorCode.INVALID_EMAIL_VERIFICATION_CODE);
        }
        redisTemplate.delete(redisKey);
    }

    /**
     * 패스워드 리셋용 토큰을 반환하는 메서드
     * @param email 비밀번호를 바꾸고자 하는 이메일
     * @return 패스워드 리셋용 토큰(passwordService 에서 사용)
     */
    public VerifyPasswordResetCodeResponseDto createPasswordResetToken(String email) {
        User user = userFindByService.findByEmail(email);
        String redisKey = PASSWORD_TOKEN_PREFIX + ":" + user.getEmail();
        String token = SecureRandomUtil.generateSecureToken();
        redisTemplate.opsForValue().set(redisKey, token, PASSWORD_TOKEN_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        return VerifyPasswordResetCodeResponseDto.of(email, token);
    }

    /**
     * 이메일 인증코드 생성
     */
    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
