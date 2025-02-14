package com.project.cheerha.domain.auth.service;

import com.project.cheerha.common.exception.auth.AuthErrorCode;
import com.project.cheerha.common.exception.auth.UnAuthorizedException;
import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
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

    //TODO: signUp도 login 처럼 사용자 차단 고려
    public CreateSignupResponseDto signup(CreateSignupRequestDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new BadRequestException(ClientErrorCode.ALREADY_EXIST_EMAIL);
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

    //TODO: aop 기능 : 사용자 ip 추출 하고, 한 ip 에서 같은 email 로 n번 이상 로그인 실패 시 해당 아이디에 대한 로그인 일시 차단
    public CreateLoginResponseDto login(CreateLoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.email())
            .orElseThrow(() -> new UnAuthorizedException(AuthErrorCode.WRONG_EMAIL_OR_PASSWORD));
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new UnAuthorizedException(AuthErrorCode.WRONG_EMAIL_OR_PASSWORD);
        }

        String accessToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        redisRefreshTokenService.createRefreshToken(user.getId(), refreshToken);

        return CreateLoginResponseDto.of(accessToken, refreshToken);
    }

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

    public RefreshAccessTokenResponseDto refreshAccessToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED);
        }
        //refreshToken 접두어 제거(claims 위함)
        refreshToken = jwtUtil.substringToken(refreshToken);

        Claims claims;

        try {
            claims = jwtUtil.extractClaims(refreshToken);
        } catch (Exception e) {
            throw new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED);
        }

        Long userId = Long.parseLong(claims.getSubject());

        String storedRefreshToken = redisRefreshTokenService.getRefreshToken(userId);

        if (storedRefreshToken == null) {
            log.error("Refresh Token not found in Redis for userId: {}", userId);
            throw new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED);
        }

        //storedToken 도 접두어 제거 한 상태로 비교해야 함
        if (!refreshToken.equals(jwtUtil.substringToken(storedRefreshToken))) {
            log.error("Refresh Token mismatch for userId: {}", userId);
            throw new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED);
        }

        //refreshToken 도 새로 발급 -> 재사용 막음
        String newRefreshToken = jwtUtil.createRefreshToken(userId);
        redisRefreshTokenService.createRefreshToken(userId, newRefreshToken);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(DataErrorCode.USER_NOT_FOUND));

        String refreshAccessToken = jwtUtil.createToken(userId, user.getEmail(), user.getRole());
        return RefreshAccessTokenResponseDto.of(refreshAccessToken);
    }
}
