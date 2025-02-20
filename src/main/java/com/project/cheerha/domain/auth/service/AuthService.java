package com.project.cheerha.domain.auth.service;

import com.project.cheerha.common.exception.auth.AuthErrorCode;
import com.project.cheerha.common.exception.auth.UnAuthorizedException;
import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.properties.JwtSecurityProperties;
import com.project.cheerha.common.redis.RedisBlackListService;
import com.project.cheerha.common.redis.RedisRefreshTokenService;
import com.project.cheerha.common.util.JwtUtil;
import com.project.cheerha.common.util.PasswordEncoder;
import com.project.cheerha.domain.auth.dto.request.CreateLoginRequestDto;
import com.project.cheerha.domain.auth.dto.request.CreateSignupRequestDto;
import com.project.cheerha.domain.auth.dto.response.CreateLoginResponseDto;
import com.project.cheerha.domain.auth.dto.response.CreateLogoutResponseDto;
import com.project.cheerha.domain.auth.dto.response.CreateSignupResponseDto;
import com.project.cheerha.domain.auth.dto.response.RefreshAccessTokenResponseDto;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.repository.UserRepository;
import com.project.cheerha.domain.user.service.UserFindByService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtSecurityProperties jwtSecurityProperties;
    private final RedisRefreshTokenService redisRefreshTokenService;
    private final RedisBlackListService redisBlackListService;
    private final UserFindByService userFindByService;

    /**
     * TODO: 비정상적인 사용자 차단 고려
     * 회원가입 처리하는 메서드
     * @throws BadRequestException 이메일이 이미 존재하는 경우
     */
    public CreateSignupResponseDto signup(CreateSignupRequestDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new BadRequestException(ClientErrorCode.ALREADY_EXIST_EMAIL);
        }
        String encodedPassword = passwordEncoder.encode(dto.password());

        User user = User.of(
            dto.email(),
            dto.name(),
            dto.age(),
            dto.career(),
            encodedPassword
        );
        userRepository.save(user);
        return CreateSignupResponseDto.of();
    }

    /**
     * TODO: aop 기능 : 사용자 ip 추출 하고, 한 ip 에서 같은 email 로 n번 이상 로그인 실패 시 해당 아이디에 대한 로그인 일시 차단
     * 로그인 메서드 - AccessToken 과 RefreshToken 생성
     * @return 로그인 응답 객체(AccessToken, RefreshToken 포함)
     */
    public CreateLoginResponseDto login(CreateLoginRequestDto dto) {
        User user = userFindByService.findByEmail(dto.email());
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new UnAuthorizedException(AuthErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtUtil.createToken(user.getId(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        redisRefreshTokenService.createRefreshToken(user.getId(), refreshToken);

        return CreateLoginResponseDto.of(accessToken, refreshToken);
    }

    /**
     * 로그아웃 메서드 - AccessToken 을 BlackList 에 추가하고, RefreshToken 을 삭제
     * @param authHeader 인증 헤더(prefix 포함된 token)
     * @throws UnAuthorizedException 토큰이 유효하지 않은 경우
     */
    public CreateLogoutResponseDto logout(String authHeader) {
        String prefix = jwtSecurityProperties.getToken().getPrefix();
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(prefix)) {
            throw new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED);
        }
        String token = jwtUtil.substringToken(authHeader);
        Claims claims = jwtUtil.extractClaims(token);
        long expirationMillis = claims.getExpiration().getTime() - System.currentTimeMillis();

        if (expirationMillis > 0) {
            redisBlackListService.addToBlackList(token);
        }
        Long userId = Long.parseLong(jwtUtil.extractClaims(token).getSubject());
        redisRefreshTokenService.deleteRefreshToken(userId);

        return CreateLogoutResponseDto.of();
    }

    /**
     * 새로운 AccessToken 을 발급하는 메서드, 사용된 RefreshToken 도 재발급
     * @param refreshToken 현재 사용자의 RefreshToken
     * @return 새로운 AccessToken
     * @throws UnAuthorizedException 토큰이 유효하지 않거나 - 저장된 값과 다를 경우
     */
    public RefreshAccessTokenResponseDto refreshAccessToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED);
        }
        refreshToken = jwtUtil.substringToken(refreshToken);

        Claims claims;

        try {
            claims = jwtUtil.extractClaims(refreshToken);
        } catch (Exception e) {
            throw new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED);
        }

        Long userId = Long.parseLong(claims.getSubject());

        String storedRefreshToken = redisRefreshTokenService.getRefreshToken(userId);

        if (storedRefreshToken == null || storedRefreshToken.isBlank()) {
            log.error("Refresh Token not found in Redis for userId: {}", userId);
            throw new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED);
        }

        if (!refreshToken.equals(jwtUtil.substringToken(storedRefreshToken))) {
            log.error("Refresh Token mismatch for userId: {}", userId);
            throw new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED);
        }

        String newRefreshToken = jwtUtil.createRefreshToken(userId);
        redisRefreshTokenService.createRefreshToken(userId, newRefreshToken);

        User user = userFindByService.findById(userId);

        String refreshAccessToken = jwtUtil.createToken(userId, user.getRole());
        return RefreshAccessTokenResponseDto.of(refreshAccessToken);
    }
}
