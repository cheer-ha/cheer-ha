package com.project.cheerha.common.scheduler.consumer;

import com.project.cheerha.common.redis.redisson.RedissonRepository;
import com.project.cheerha.common.scheduler.core.InstanceManager;
import com.project.cheerha.common.scheduler.core.TaskHandler;
import com.project.cheerha.common.scheduler.repository.TaskRepository;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TaskConsumer {

    private final InstanceManager instanceManager;
    private final TaskRepository taskRepository;
    private final RedissonRepository redissonRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, TaskHandler> handlers = new HashMap<>();

    private static final String SORTED_SET_KEY = "scheduled-tasks";
    private volatile boolean isRunning = true;

    /**
     * 생성자를 초기화 할 때 TaskHandler 의 구현체들을 가져옴
     */
    public TaskConsumer(InstanceManager instanceManager, TaskRepository taskRepository, RedissonRepository redissonRepository, List<TaskHandler> handlerList) {
        this.instanceManager = instanceManager;
        this.taskRepository = taskRepository;
        this.redissonRepository = redissonRepository;
        handlerList.forEach(handler -> handlers.put(handler.getTaskType(), handler));
    }

    /**
     * Redis 에서 실행 시점이 된 작업들을 가져와서 실행함
     */
    @Scheduled(fixedDelay = 5000)
    public void processDueTasks() {
        instanceManager.updateLatestInstance();
        //최신 인스턴스가 아니면 작업 실행을 건너뛰기
        if (!instanceManager.isLatestInstance()) {
            return;
        }
        if (!isRunning) return;

        String taskDataString = taskRepository.getDueTask(SORTED_SET_KEY);
        if (taskDataString == null) {
            return;
        }

        try {
            Map<String, Object> taskData = objectMapper.readValue(taskDataString, Map.class);
            String taskType = (String) taskData.get("taskType");
            Map<String, Object> payload = (Map<String, Object>) taskData.get("payload");

            TaskHandler handler = handlers.get(taskType);
            if (handler != null) {
                long scheduleIntervalMillis = handler.getScheduleIntervalMillis();
                if (scheduleIntervalMillis <= 0) return;
                String lockKey = "scheduler:lock:consumer" + handler.getTaskType();
                try {
                    if (redissonRepository.tryLock(lockKey, Math.min(2000, scheduleIntervalMillis / 2), scheduleIntervalMillis / 2, TimeUnit.MILLISECONDS)) {
                        try {
                            handler.handle(payload);
                            log.info("TaskConsumer: 작업 완료: {} at {}", taskType, Instant.now());
                        } catch (Exception e) {
                            log.error("TaskConsumer: 작업 실행 중 오류: {}", e.getMessage(), e);
                        } finally {
                            redissonRepository.unlock(lockKey);
                        }
                    } else {
                        log.warn("TaskConsumer: 락 획득 실패, 작업 스킵: {}", taskType);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("TaskConsumer: 락 획득 중 인터럽트 발생: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("TaskConsumer: 데이터 파싱 중 오류: {}", e.getMessage(), e);
        }
    }

    @PreDestroy
    public void shutdown() {
        isRunning = false;
    }
}