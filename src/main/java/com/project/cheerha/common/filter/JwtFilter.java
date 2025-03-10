package com.project.cheerha.common.filter;

import com.project.cheerha.common.exception.handler.FilterExceptionHandler;
import com.project.cheerha.common.properties.JwtSecurityProperties;
import com.project.cheerha.common.redis.auth.RedisBlackListService;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtSecurityProperties securityProperties;
    private final RedisBlackListService redisBlackListService;
    private final JwtUtil jwtUtil;
    private final FilterExceptionHandler filterExceptionHandler;

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
            filterExceptionHandler.sendErrorResponse(httpResponse, HttpStatus.BAD_REQUEST, "JWT 토큰이 필요합니다.");
            return;
        }

        String token = jwtUtil.substringToken(bearerJwt);

        if (redisBlackListService.isBlackList(token)) {
            filterExceptionHandler.sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED, "블랙리스트된 토큰입니다.");
            return;
        }

        try {
            Claims claims = jwtUtil.extractClaims(token);
            if (claims == null || claims.getSubject() == null) {
                filterExceptionHandler.sendErrorResponse(httpResponse, HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다.");
                return;
            }

            String[] decryptedData = claims.getSubject().split(":");
            Long userId = Long.valueOf(decryptedData[0]);
            httpRequest.setAttribute("userId", userId);

            if (bearerJwt.startsWith(securityProperties.token().prefix()) && decryptedData.length > 1) {
                Role role = Role.valueOf(decryptedData[1]);
                httpRequest.setAttribute("userRole", role);
            }

            if (isRequiredAdmin(url)) {
                Role role = (Role) httpRequest.getAttribute("userRole");
                if (role == null || !role.equals(Role.ADMIN)) {
                    filterExceptionHandler.sendErrorResponse(httpResponse, HttpStatus.FORBIDDEN, "관리자만 접근 가능합니다.");
                    return;
                }
            }

            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            filterExceptionHandler.sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않은 JWT 서명 입니다.", e);
            filterExceptionHandler.sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            filterExceptionHandler.sendErrorResponse(httpResponse, HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("Invalid JWT token, 유효하지 않은 JWT 토큰 입니다.", e);
            filterExceptionHandler.sendErrorResponse(httpResponse, HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰 형식입니다.");
        } catch (Exception e) {
            log.error("예상치 못한 예외 발생", e);
            filterExceptionHandler.sendErrorResponse(httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
        }
    }

    private boolean isWhiteList(String requestURI) {
        return securityProperties.secret().whiteList().contains(requestURI);
    }

    private boolean isRequiredAdmin(String requestURI) {
        return securityProperties.secret().adminList().contains(requestURI);
    }
}
