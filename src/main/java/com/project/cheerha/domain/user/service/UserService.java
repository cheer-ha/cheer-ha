package com.project.cheerha.domain.user.service;

import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
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
            .orElseThrow(() -> new NotFoundException(DataErrorCode.USER_NOT_FOUND));
        return ReadUserResponseDto.of(user.getEmail(), user.getName(), user.getCareer());
    }
}
