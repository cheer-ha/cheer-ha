package com.project.cheerha.common.config;

import com.project.cheerha.common.exception.handler.FilterExceptionHandler;
import com.project.cheerha.domain.auth.repository.BannedIpRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpBlockingFilter implements Filter {

    private final BannedIpRepository bannedIpRepository;
    private final FilterExceptionHandler filterExceptionHandler;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String ip = getClientIp(httpRequest);

        if (bannedIpRepository.existsByIp(ip)) {
            log.warn("차단된 IP 접근 시도: {}", ip);
            filterExceptionHandler.sendErrorResponse(httpResponse, HttpStatus.FORBIDDEN, "차단된 IP입니다.");
            return;
        }

        chain.doFilter(request, response);
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
