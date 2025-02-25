package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.util.PasswordEncoder;
import com.project.cheerha.domain.user.dto.request.UpdatePasswordRequestDto;
import com.project.cheerha.domain.user.dto.request.UpdatePasswordWithEmailRequestDto;
import com.project.cheerha.domain.user.dto.response.ReadUserResponseDto;
import com.project.cheerha.domain.user.dto.response.UpdatePasswordResponseDto;
import com.project.cheerha.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserFindByService userFindByService;
    private final PasswordEncoder passwordEncoder;

    public ReadUserResponseDto readUser(AuthUser authUser) {
        User user = userFindByService.findById(authUser.id());

        return ReadUserResponseDto.of(user.getEmail(), user.getName(), user.getCareer(), user.getAge());
    }

    @Transactional
    public UpdatePasswordResponseDto updatePassword(Long id, UpdatePasswordRequestDto requestDto) {
        User user = userFindByService.findById(id);
        if(!passwordEncoder.matches(requestDto.oldPassword(), user.getPassword())) {
            throw new BadRequestException(ClientErrorCode.INVALID_CURRENT_PASSWORD);
        }
        user.updatePassword(passwordEncoder.encode(requestDto.newPassword()));
        return UpdatePasswordResponseDto.of();
    }

    //TODO: 다음 풀리퀘스트
    public UpdatePasswordResponseDto updatePasswordWithEmailVerification(UpdatePasswordWithEmailRequestDto requestDto) {
        return null;
    }
}
