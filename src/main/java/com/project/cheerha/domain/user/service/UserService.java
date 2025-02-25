package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.user.dto.request.UpdatePasswordRequestDto;
import com.project.cheerha.domain.user.dto.request.UpdatePasswordWithEmailRequestDto;
import com.project.cheerha.domain.user.dto.response.ReadUserResponseDto;
import com.project.cheerha.domain.user.dto.response.UpdatePasswordResponseDto;
import com.project.cheerha.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserFindByService userFindByService;

    public ReadUserResponseDto readUser(AuthUser authUser) {
        User user = userFindByService.findById(authUser.id());

        return ReadUserResponseDto.of(user.getEmail(), user.getName(), user.getCareer(), user.getAge());
    }

    //TODO: 다음 풀리퀘스트
    public UpdatePasswordResponseDto updatePassword(AuthUser authUser, UpdatePasswordRequestDto requestDto) {
        return null;
    }

    //TODO: 다음 풀리퀘스트
    public UpdatePasswordResponseDto updatePasswordWithEmailVerification(UpdatePasswordWithEmailRequestDto requestDto) {
        return null;
    }
}
