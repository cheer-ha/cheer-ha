package com.project.cheerha.common.interceptor;

import com.project.cheerha.common.exception.auth.AuthErrorCode;
import com.project.cheerha.common.exception.auth.UnAuthorizedException;
import com.project.cheerha.domain.auth.repository.BannedIpRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpBlockingInterceptor implements HandlerInterceptor {

    private final BannedIpRepository bannedIpRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ip = getClientIp(request);

        if (bannedIpRepository.existsByIp(ip)) {
            log.warn("차단된 IP 접근 시도: {}", ip);
            throw new UnAuthorizedException(AuthErrorCode.BANNED_IP);
        }
        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            ip = ip.split(",")[0].trim();
        } else {
            ip = request.getRemoteAddr();
        }

        if (ip.contains(":")) {
            log.info("IPv6 추출 성공: {}", ip);
        } else {
            log.info("IPv4 추출 성공: {}", ip);
        }
        return ip;
    }
}
