package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.domain.notice.service.EmailSender;
import com.project.cheerha.domain.user.dto.response.ActivateNotificationResponseDto;
import com.project.cheerha.domain.user.dto.response.VerifyPasswordResetTokenResponseDto;
import com.project.cheerha.domain.user.dto.response.SendEmailVerificationResponseDto;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.cheerha.domain.user.service.TokenService.NOTIFICATION_VERIFICATION_TOKEN_PREFIX;
import static com.project.cheerha.domain.user.service.TokenService.PASSWORD_VERIFICATION_TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
public class UserEmailVerificationService {

    private final EmailSender emailSender;
    private final UserFindByService userFindByService;
    private final UserRepository userRepository;
    private final CheckDailyEmailCount checkDailyEmailCount;
    private final TokenService tokenService;

    /**
     * 이메일 알림을 받기 위해 이메일인증을 보내는 메서드
     * @param id 현재 사용자의 id
     */
    public SendEmailVerificationResponseDto sendNotificationVerifyEmailVerificationToken(Long id) {
        User user = userFindByService.findById(id);
        if(user.isNotificationEnabled()){
            throw new BadRequestException(ClientErrorCode.ALREADY_VERIFIED_EMAIL);
        }
        checkDailyEmailCount.incrementDailyLimit(user.getEmail(), NOTIFICATION_VERIFICATION_TOKEN_PREFIX);
        String token = tokenService.saveToken(NOTIFICATION_VERIFICATION_TOKEN_PREFIX, user.getEmail());
        emailSender.sendVerificationEmail(user.getEmail(), token);
        return SendEmailVerificationResponseDto.toDto();
    }

    /**
     * 이메일 알림 인증 코드 검증용 메서드
     * @param token 사용자의 이메일에 보낸 코드
     */
    public void verifyNotificationEmailToken(Long id, String token) {
        User user = userFindByService.findById(id);
        tokenService.verifyNotificationEmailToken(user.getEmail(), token);
    }

    /**
     * 이메일 알림 허용유무를 업데이트하는 메서드
     * @param id 현재사용자의 id
     */
    @Transactional
    public ActivateNotificationResponseDto activateNotification(Long id) {
        User user = userFindByService.findById(id);
        user.updateNotificationEnabled();
        return ActivateNotificationResponseDto.toDto();
    }

    /**
     * 비로그인 사용자의 패스워드 리셋을 위해 이메일인증을 보내는 메서드
     * @param email 비밀번호를 바꾸고자 하는 이메일
     */
    public SendEmailVerificationResponseDto sendPasswordResetEmailVerificationToken(String email) {
        if(!userRepository.existsByEmail(email)){
            throw new NotFoundException(DataErrorCode.USER_NOT_FOUND);
        }
        checkDailyEmailCount.incrementDailyLimit(email, PASSWORD_VERIFICATION_TOKEN_PREFIX);
        String token = tokenService.saveToken(PASSWORD_VERIFICATION_TOKEN_PREFIX, email);
        emailSender.sendVerificationEmail(email, token);
        return SendEmailVerificationResponseDto.toDto();
    }

    public void verifyPasswordResetEmailToken(String email, String token) {
        if(!userRepository.existsByEmail(email)){
            throw new NotFoundException(DataErrorCode.USER_NOT_FOUND);
        }
        tokenService.verifyPasswordResetEmailToken(email, token);
    }

    /**
     * 패스워드 리셋용 토큰을 반환하는 메서드
     * @param email 비밀번호를 바꾸고자 하는 이메일
     * @return 패스워드 리셋용 토큰(passwordService 에서 사용)
     */
    public VerifyPasswordResetTokenResponseDto createPasswordResetToken(String email) {
        if(!userRepository.existsByEmail(email)){
            throw new NotFoundException(DataErrorCode.USER_NOT_FOUND);
        }
        String token = tokenService.saveSecureToken(email);
        return VerifyPasswordResetTokenResponseDto.toDto(email, token);
    }

}
