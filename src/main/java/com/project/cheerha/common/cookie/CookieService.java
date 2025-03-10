package com.project.cheerha.common.cookie;

import com.project.cheerha.common.exception.auth.AuthErrorCode;
import com.project.cheerha.common.exception.auth.UnAuthorizedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class CookieService {

    /**
     * refreshToken 을 쿠키에 저장합니다
     */
    public void createRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshTokenCookie);
    }

    /**
     * 쿠키에 저장된 refreshToken 을 가져옵니다
     */
    public String getRefreshTokenCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED);
        }
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName()) && StringUtils.hasText(cookie.getValue())) {
                return cookie.getValue();
            }
        }
        throw new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED);
    }

    /**
     * 쿠키에 저장된 refreshToken 을 삭제합니다
     */
    public void removeRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(refreshTokenCookie);
    }

}
