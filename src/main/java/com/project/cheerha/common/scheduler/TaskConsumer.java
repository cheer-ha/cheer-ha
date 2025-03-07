package com.project.cheerha.common.scheduler;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cheerha.common.util.InstanceUtil;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TaskConsumer {

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, TaskHandler> handlers = new HashMap<>();
    private static final String SORTED_SET_KEY = "scheduled-tasks";
    private static final String LATEST_INSTANCE_KEY = "scheduler:latest-instance";
    private volatile boolean isRunning = true;

    public TaskConsumer(RedissonClient redissonClient, List<TaskHandler> handlerList) {
        this.redissonClient = redissonClient;
        handlerList.forEach(handler -> handlers.put(handler.getTaskType(), handler));
    }

    @Scheduled(fixedDelay = 5000)
    public void processDueTasks() {
        // 최신 인스턴스가 아니면 작업 실행을 건너뛰기
        if (!isLatestInstance()) {
            log.debug("현재 인스턴스는 최신 인스턴스가 아니므로 작업 실행을 건너뜁니다.");
            return;
        }

        if (!isRunning) return;

        String taskDataStr = getDueTask();
        if (taskDataStr != null) {
            try {
                Map<String, Object> taskData = objectMapper.readValue(taskDataStr, Map.class);
                String taskType = (String) taskData.get("taskType");
                Map<String, Object> payload = (Map<String, Object>) taskData.get("payload");

                TaskHandler handler = handlers.get(taskType);
                if (handler != null) {
                    handler.handle(payload);
                    log.info("TaskConsumer: 작업 시작: {} at {}", taskType, Instant.now());
                } else {
                    log.info("TaskConsumer: 작업 없음: {}", taskType);
                }
            } catch (Exception e) {
                log.info("TaskConsumer: 작업 실행 중 오류: {}", e.getMessage());
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        isRunning = false;
    }

    private String getDueTask() {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(SORTED_SET_KEY);
        long currentTime = Instant.now().toEpochMilli();
        // 현재 시간까지의 작업들을 조회
        Collection<String> dueTasks = sortedSet.valueRange(0, true, currentTime, true);

        if (dueTasks != null && !dueTasks.isEmpty()) {
            // 첫 번째 작업을 꺼내고 제거
            String task = dueTasks.iterator().next();
            sortedSet.remove(task);
            return task;
        }
        return null;
    }

    // CentralScheduler와 유사하게 현재 인스턴스가 최신인지 확인하는 메서드
    private boolean isLatestInstance() {
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
}
