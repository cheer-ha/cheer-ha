package com.project.cheerha.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
public class HealthCheckUtil {

    /**
     * 현재 인스턴스의 헬스 체크를 수행해서 actuator/health 가 UP 상태인지 확인
     */
    public static boolean isInstanceHealthy() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String healthUrl = "http://localhost:8080/actuator/health"; // 필요에 따라 변경
            ResponseEntity<Map> response = restTemplate.getForEntity(healthUrl, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object statusObj = response.getBody().get("status");
                if (statusObj != null && "UP".equalsIgnoreCase(statusObj.toString())) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("헬스 체크 실패", e);
        }
        return false;
    }
}
