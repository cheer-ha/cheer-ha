package com.project.cheerha.domain.user.service;

import com.project.cheerha.domain.user.dto.request.SendEmailVerificationRequestDto;
import com.project.cheerha.domain.user.dto.response.ActivateNotificationResponseDto;
import com.project.cheerha.domain.user.dto.response.CreatePasswordResetTokenResponseDto;
import com.project.cheerha.domain.user.dto.response.SendEmailVerificationResponseDto;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationService {
    public SendEmailVerificationResponseDto sendVerificationCode(SendEmailVerificationRequestDto requestDto) {
        return null;
    }

    public boolean verifyEmailCode(String email, String code) {
        return true;
    }

    public CreatePasswordResetTokenResponseDto createPasswordResetToken(String email) {
        return null;
    }

    public ActivateNotificationResponseDto activateNotifications(String email) {
        return null;
    }
}
