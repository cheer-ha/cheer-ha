package com.project.cheerha.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cheerha.common.properties.JwtSecurityProperties;
import com.project.cheerha.common.redis.RedisBlackListService;
import com.project.cheerha.common.util.JwtUtil;

import com.project.cheerha.domain.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.PatternMatchUtils;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private static final String[] WHITE_LIST = {"/auth/signup", "/auth/login", "/actuator/health", "/actuator/prometheus"};
    private final JwtSecurityProperties securityProperties;
    private final RedisBlackListService redisBlackListService;
    private final JwtUtil jwtUtil;

    /**
     * WhiteList 이외의 Http 요청에서 Jwt 토큰을 확인하고, 유효성 검사 수행한 후, 사용자 정보 설정
     * AccessToken - UserId, Role 저장
     * RefreshToken - UserId 만 저장
     * @param request http 요청
     * @param response http 응답
     * @param chain 필터체인(다음 필터로 요청 전달)
     * @throws IOException 입출력 예외 발생 시
     * @throws ServletException 서블릿 예외 발생 시(토큰 관련 오류)
     */
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
            sendErrorResponse(httpResponse, HttpStatus.BAD_REQUEST, "JWT 토큰이 필요합니다.");
            return;
        }

        String token = jwtUtil.substringToken(bearerJwt);

        if (redisBlackListService.isBlackList(token)) {
            sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED, "블랙리스트된 토큰입니다.");
            return;
        }

        try {
            Claims claims = jwtUtil.extractClaims(token);
            if (claims == null) {
                sendErrorResponse(httpResponse, HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다.");
                return;
            }

            httpRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));

            if (bearerJwt.startsWith(securityProperties.getToken().getPrefix())) {
                String userRoleString = claims.get("userRole", String.class);
                Role role = Role.valueOf(userRoleString);

                if (userRoleString != null) {
                    httpRequest.setAttribute("userRole", role);
                }
            }

            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않은 JWT 서명 입니다.", e);
            sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            sendErrorResponse(httpResponse, HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("Invalid JWT token, 유효하지 않은 JWT 토큰 입니다.", e);
            sendErrorResponse(httpResponse, HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰 형식입니다.");
        } catch (Exception e) {
            log.error("예상치 못한 예외 발생", e);
            sendErrorResponse(httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
        }
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);

        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }

}
