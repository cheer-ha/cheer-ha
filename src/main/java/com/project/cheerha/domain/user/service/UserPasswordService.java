package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.util.PasswordEncoder;
import com.project.cheerha.domain.user.dto.request.ResetPasswordRequestDto;
import com.project.cheerha.domain.user.dto.request.UpdatePasswordRequestDto;
import com.project.cheerha.domain.user.dto.response.UpdatePasswordResponseDto;
import com.project.cheerha.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPasswordService {

    private final UserFindByService userFindByService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String PASSWORD_TOKEN_PREFIX = "password_verification";

    @Transactional
    public UpdatePasswordResponseDto updatePassword(Long id, UpdatePasswordRequestDto requestDto) {
        User user = userFindByService.findById(id);
        if(!passwordEncoder.matches(requestDto.oldPassword(), user.getPassword())) {
            throw new BadRequestException(ClientErrorCode.INVALID_CURRENT_PASSWORD);
        }
        user.updatePassword(passwordEncoder.encode(requestDto.newPassword()));
        return UpdatePasswordResponseDto.of();
    }

    @Transactional
    public UpdatePasswordResponseDto resetPassword(ResetPasswordRequestDto requestDto) {
        String redisKey = PASSWORD_TOKEN_PREFIX + ":" + requestDto.email();
        String storedToken = redisTemplate.opsForValue().get(redisKey);

        if (storedToken == null || !storedToken.equals(requestDto.token())) {
            throw new BadRequestException(ClientErrorCode.INVALID_PASSWORD_RESET_TOKEN);
        }

        User user = userFindByService.findByEmail(requestDto.email());
        user.updatePassword(passwordEncoder.encode(requestDto.newPassword()));

        redisTemplate.delete(redisKey);

        return UpdatePasswordResponseDto.of();
    }
}
