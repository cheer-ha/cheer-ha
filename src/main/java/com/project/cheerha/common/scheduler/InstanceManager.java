package com.project.cheerha.common.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cheerha.common.util.InstanceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InstanceManager {

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;
    private static final String LATEST_INSTANCE_KEY = "scheduler:latest-instance";

    public boolean isLatestInstance() {
        try {
            String latestInstanceJson = (String) redissonClient.getBucket(LATEST_INSTANCE_KEY).get();
            if (latestInstanceJson == null) {
                return true;
            }
            Map<String, String> latestInstance = objectMapper.readValue(latestInstanceJson, Map.class);
            String latestInstanceId = latestInstance.get("instanceId");
            long latestStartTime = Long.parseLong(latestInstance.get("startTime"));

            long currentStartTime = InstanceUtil.getInstanceStartTime().toEpochMilli();

            return InstanceUtil.getInstanceId().equals(latestInstanceId)
                    || currentStartTime > latestStartTime;
        } catch (Exception e) {
            log.error("인스턴스 확인 중 오류 발생", e);
            return false;
        }
    }

    public void updateLatestInstance() {
        try {
            //Redis 에서 최신 인스턴스 정보 가져오기
            String latestInstanceJson = (String) redissonClient.getBucket(LATEST_INSTANCE_KEY).get();
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

            redissonClient.getBucket(LATEST_INSTANCE_KEY).set(objectMapper.writeValueAsString(newLatestInstance));
            log.info("최신 인스턴스로 등록됨: {}", newLatestInstance);
        } catch (Exception e) {
            log.error("최신 인스턴스 등록 중 오류 발생", e);
        }
    }

}
