package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.common.exception.CustomException;
import com.project.cheerha.common.exception.ErrorCode;
import com.project.cheerha.domain.user.dto.response.ReadUserResponseDto;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ReadUserResponseDto readUser(AuthUser authUser) {
        User user = userRepository.findById(authUser.id())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return ReadUserResponseDto.of(user.getEmail(), user.getName(), user.getCareer());
    }
}
