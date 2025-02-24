package com.project.cheerha.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpUtil {

    public static String getClientIp(HttpServletRequest request) {
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
