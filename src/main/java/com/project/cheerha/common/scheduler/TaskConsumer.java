package com.project.cheerha.common.scheduler;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TaskConsumer {

    private final InstanceManager instanceManager;
    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, TaskHandler> handlers = new HashMap<>();

    private static final String SORTED_SET_KEY = "scheduled-tasks";
    private volatile boolean isRunning = true;

    /**
     * 생성자를 초기화 할 떄 TaskHandler 의 구현체들을 가져옴
     */
    public TaskConsumer(InstanceManager instanceManager, RedissonClient redissonClient, List<TaskHandler> handlerList) {
        this.instanceManager = instanceManager;
        this.redissonClient = redissonClient;
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

        String taskDataStr = getDueTask();
        if (taskDataStr != null) {
            try {
                Map<String, Object> taskData = objectMapper.readValue(taskDataStr, Map.class);
                String taskType = (String) taskData.get("taskType");
                Map<String, Object> payload = (Map<String, Object>) taskData.get("payload");

                TaskHandler handler = handlers.get(taskType);
                if (handler != null) {
                    handler.handle(payload);
                    log.info("TaskConsumer: 작업 완료: {} at {}", taskType, Instant.now());
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

    /**
     * 현재 시간보다 작거나 같은 score 를 가진 작업들을 확인
     * @return 해당 조건을 충족하는 작업(실행 시켜야 할 작업)
     */
    private String getDueTask() {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(SORTED_SET_KEY);
        long currentTime = Instant.now().toEpochMilli();
        //현재 시간까지의 작업들을 조회
        Collection<String> dueTasks = sortedSet.valueRange(0, true, currentTime, true);

        if (dueTasks != null && !dueTasks.isEmpty()) {
            //첫 번째 작업을 꺼내고 제거
            String task = dueTasks.iterator().next();
            sortedSet.remove(task);
            return task;
        }
        return null;
    }
}
