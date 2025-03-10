package com.project.cheerha.common.scheduler.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cheerha.common.scheduler.repository.TaskRepository;
import com.project.cheerha.common.util.HealthCheckUtil;
import com.project.cheerha.common.util.InstanceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InstanceManager {

    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;
    private static final String LATEST_INSTANCE_KEY = "scheduler:latest-instance";

    /**
     * 현재 인스턴스가 최신 인스턴스인지 확인
     */
    public boolean isLatestInstance() {
        if (!HealthCheckUtil.isInstanceHealthy()) {
            log.warn("현재 인스턴스 헬스 체크 실패");
            return false;
        }
        try {
            String latestInstanceJson = taskRepository.getValue(LATEST_INSTANCE_KEY);
            if (latestInstanceJson == null) {
                return true;
            }
            Map<String, String> latestInstance = objectMapper.readValue(latestInstanceJson, Map.class);
            String latestInstanceId = latestInstance.get("instanceId");
            long latestStartTime = Long.parseLong(latestInstance.get("startTime"));

            long currentStartTime = InstanceUtil.getInstanceStartTime().toEpochMilli();

            return InstanceUtil.getInstanceId().equals(latestInstanceId) || currentStartTime > latestStartTime;
        } catch (Exception e) {
            log.error("인스턴스 확인 중 오류 발생", e);
            return false;
        }
    }

    /**
     * Redis 에 담긴 최신 인스턴스 정보와 현재 인스턴스를 비교 후 업데이트
     */
    public void updateLatestInstance() {
        try {
            //Redis 에서 최신 인스턴스 정보 가져오기
            String latestInstanceJson = taskRepository.getValue(LATEST_INSTANCE_KEY);
            long currentStartTime = InstanceUtil.getInstanceStartTime().toEpochMilli();
            String currentInstanceId = InstanceUtil.getInstanceId();

            if (latestInstanceJson != null) {
                Map<String, String> latestInstance = objectMapper.readValue(latestInstanceJson, Map.class);
                long latestStartTime = Long.parseLong(latestInstance.get("startTime"));

                //Redis 에 인스턴스가 더 최신이면 갱신하지 않음
                if (latestStartTime >= currentStartTime) {
                    return;
                }
            }

            //최신 인스턴스로 등록
            Map<String, String> newLatestInstance = Map.of(
                    "instanceId", currentInstanceId,
                    "startTime", String.valueOf(currentStartTime)
            );

            taskRepository.setValue(LATEST_INSTANCE_KEY, objectMapper.writeValueAsString(newLatestInstance));
            log.info("최신 인스턴스로 등록됨: {}", newLatestInstance);
        } catch (Exception e) {
            log.error("최신 인스턴스 등록 중 오류 발생", e);
        }
    }
}
