package com.project.cheerha.common.filter;

import com.project.cheerha.common.exception.handler.FilterExceptionHandler;
import com.project.cheerha.common.repository.KeyValueQueryRepository;
import com.project.cheerha.common.util.IpUtil;
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

    private final FilterExceptionHandler filterExceptionHandler;
    private final KeyValueQueryRepository keyValueQueryRepository;

    private static final String BLOCK_PREFIX = "block:ip:";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String ip = IpUtil.getClientIp(httpRequest);
        String redisBlockKey = BLOCK_PREFIX + ip;

        if (Boolean.TRUE.equals(keyValueQueryRepository.hasKey(redisBlockKey))) {
            log.warn("차단된 IP 로그인 시도: {}", ip);
            filterExceptionHandler.sendErrorResponse(httpResponse, HttpStatus.FORBIDDEN, "30초간 차단된 IP입니다.");
            return;
        }

        chain.doFilter(request, response);
    }
}
