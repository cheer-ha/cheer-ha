package com.project.cheerha.common.config;

import com.project.cheerha.common.exception.handler.FilterExceptionHandler;
import com.project.cheerha.common.util.IpUtil;
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
        String ip = IpUtil.getClientIp(httpRequest);

        if (bannedIpRepository.existsByIp(ip)) {
            log.warn("차단된 IP 접근 시도: {}", ip);
            filterExceptionHandler.sendErrorResponse(httpResponse, HttpStatus.FORBIDDEN, "차단된 IP입니다. 관리자에게 문의하세요");
            return;
        }

        chain.doFilter(request, response);
    }
}
