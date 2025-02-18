package com.project.cheerha.auth;

import com.project.cheerha.common.exception.auth.AuthErrorCode;
import com.project.cheerha.common.exception.auth.UnAuthorizedException;
import com.project.cheerha.common.redis.RedisRefreshTokenService;
import com.project.cheerha.common.util.JwtUtil;
import com.project.cheerha.domain.auth.dto.response.RefreshAccessTokenResponseDto;
import com.project.cheerha.domain.auth.service.AuthService;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefreshAccessTokenTest {

    @Mock
    private UserFindByService userFindByService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RedisRefreshTokenService redisRefreshTokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    void testRefreshAccessToken_액세스토큰_재발급_성공했을때() {
        //given
        String refreshToken = "validRefreshToken";
        Claims claims = mock(Claims.class);
        User user = mock(User.class);

        when(jwtUtil.extractClaims(refreshToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("1");
        when(redisRefreshTokenService.getRefreshToken(1L)).thenReturn("validRefreshToken");
        when(jwtUtil.substringToken(refreshToken)).thenReturn("validRefreshToken");
        when(jwtUtil.substringToken("validRefreshToken")).thenReturn("validRefreshToken");
        when(jwtUtil.createRefreshToken(1L)).thenReturn("newRefreshToken");
        when(userFindByService.findById(1L)).thenReturn(user);
        when(jwtUtil.createToken(1L, user.getRole())).thenReturn("newAccessToken");

        //when
        RefreshAccessTokenResponseDto response = authService.refreshAccessToken(refreshToken);

        //then
        assertNotNull(response);
        assertEquals("newAccessToken", response.accessToken());
    }

    @Test
    void testRefreshAccessToken_존재하지_않는_토큰() {
        //given
        String invalidRefreshToken = "invalidToken";
        when(jwtUtil.extractClaims(invalidRefreshToken)).thenThrow(new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED));

        //when & then
        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class, () -> authService.refreshAccessToken(invalidRefreshToken));
        assertEquals(AuthErrorCode.TOKEN_UNAUTHORIZED.getMessage(), exception.getMessage());
    }
}
