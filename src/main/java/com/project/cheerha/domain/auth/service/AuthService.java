package com.project.cheerha.domain.auth.service;

import static com.project.cheerha.common.util.JwtUtil.expiredTokenSet;

import com.project.cheerha.common.exception.CustomException;
import com.project.cheerha.common.exception.ErrorCode;
import com.project.cheerha.common.util.JwtUtil;
import com.project.cheerha.common.util.PasswordEncoder;
import com.project.cheerha.domain.auth.dto.request.CreateLoginRequestDto;
import com.project.cheerha.domain.auth.dto.request.CreateSignupRequestDto;
import com.project.cheerha.domain.auth.dto.response.CreateLoginResponseDto;
import com.project.cheerha.domain.auth.dto.response.CreateLogoutResponseDto;
import com.project.cheerha.domain.auth.dto.response.CreateSignupResponseDto;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public CreateSignupResponseDto signup(CreateSignupRequestDto dto) {
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
        return CreateSignupResponseDto.of();
    }

    //TODO: aop 기능 : 사용자 ip 추출 하고, 한 ip 에서 같은 email 로 5번 이상 로그인 실패 시 해당 아이디에 대한 로그인 일시 차단
    public CreateLoginResponseDto login(CreateLoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.email())
            .orElseThrow(() -> new CustomException(ErrorCode.WRONG_EMAIL_OR_PASSWORD));
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_EMAIL_OR_PASSWORD);
        }
        String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());
        return CreateLoginResponseDto.of(token);
    }

    public CreateLogoutResponseDto logout(String authHeader) {
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
        }
        String token = jwtUtil.substringToken(authHeader);

        if (expiredTokenSet.contains(token)) {
            throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
        }
        expiredTokenSet.add(token);
        return CreateLogoutResponseDto.of();
    }
}
