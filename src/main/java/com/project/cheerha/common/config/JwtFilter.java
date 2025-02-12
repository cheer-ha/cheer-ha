package com.project.cheerha.common.config;

import com.project.cheerha.common.properties.JwtSecurityProperties;
import com.project.cheerha.common.redis.RedisBlackListService;
import com.project.cheerha.common.util.JwtUtil;
import com.project.cheerha.domain.user.entity.User.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private static final String[] WHITE_LIST = {"/auth/signup", "/auth/login"};
    private final JwtSecurityProperties securityProperties;
    private final RedisBlackListService redisBlackListService;
    private final JwtUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();

        if (isWhiteList(url)) {
            chain.doFilter(request, response);
            return;
        }

        String bearerJwt = httpRequest.getHeader("Authorization");

        if (bearerJwt == null) {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰이 필요합니다.");
            return;
        }

        String token = jwtUtil.substringToken(bearerJwt);

        if (redisBlackListService.isBlacklisted(token)) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "블랙리스트된 토큰입니다.");
            return;
        }

        if (JwtUtil.expiredTokenSet.contains(token)) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 유효하지 않습니다.");
            return;
        }

        try {
            Claims claims = jwtUtil.extractClaims(jwtUtil.substringToken(bearerJwt));
            if (claims == null) {
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
                return;
            }

            httpRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));

            //accessToken 일 때만 email, userRole 저장
            if (bearerJwt.startsWith(securityProperties.getToken().getPrefix())) {
                String email = claims.get("email", String.class);
                String userRoleString = claims.get("userRole", String.class);
                Role role = Role.valueOf(userRoleString);

                if (email != null && userRoleString != null) {
                    httpRequest.setAttribute("email", email);
                    httpRequest.setAttribute("userRole", role);
                }
            }

            chain.doFilter(request, response);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않은 JWT 서명 입니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
        } catch (Exception e) {
            log.error("Invalid JWT token, 유효하지 않은 JWT 토큰 입니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 JWT 토큰입니다.");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
