package com.project.cheerha.domain.auth.service;

import com.project.cheerha.common.exeption.CustomException;
import com.project.cheerha.common.exeption.ErrorCode;
import com.project.cheerha.common.util.PasswordEncoder;
import com.project.cheerha.domain.auth.dto.request.CreateLoginRequestDto;
import com.project.cheerha.domain.auth.dto.request.CreateUserRequestDto;
import com.project.cheerha.domain.auth.dto.response.CreateLoginResponseDto;
import com.project.cheerha.domain.auth.dto.response.CreateUserResponseDto;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserResponseDto signup(CreateUserRequestDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
        }
        String encodedPassword = passwordEncoder.encode(dto.password());

        User user = User.of(
            dto.email(),
            dto.name(),
            dto.career(),
            encodedPassword
        );
        userRepository.save(user);
        return CreateUserResponseDto.of("회원가입 완료");
    }

    public CreateLoginResponseDto login(CreateLoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.email())
            .orElseThrow(() -> new CustomException(ErrorCode.WRONG_EMAIL_OR_PASSWORD));
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_EMAIL_OR_PASSWORD);
        }
        String token = "토큰";
        return CreateLoginResponseDto.of(token, "로그인 성공");
    }
}
