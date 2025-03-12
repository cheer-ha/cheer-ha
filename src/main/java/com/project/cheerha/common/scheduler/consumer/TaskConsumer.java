package com.project.cheerha.common.scheduler.consumer;

import com.project.cheerha.common.scheduler.core.InstanceManager;
import com.project.cheerha.common.scheduler.core.TaskHandler;
import com.project.cheerha.common.repository.TaskRepository;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TaskConsumer {

    private final InstanceManager instanceManager;
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, TaskHandler> handlers = new HashMap<>();

    private static final String SORTED_SET_KEY = "scheduled-tasks";
    private static final String BUCKET_PREFIX = "scheduler:lastScheduledTime:";
    private volatile boolean isRunning = true;

    /**
     * 생성자를 초기화 할 떄 TaskHandler 의 구현체들을 가져옴
     */
    public TaskConsumer(InstanceManager instanceManager, TaskRepository taskRepository, List<TaskHandler> handlerList) {
        this.instanceManager = instanceManager;
        this.taskRepository = taskRepository;
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
        taskRepository.removeExpiredBuckets(BUCKET_PREFIX);
        taskRepository.removeExpiredTasks(SORTED_SET_KEY);

        String taskDataString = taskRepository.getDueTask(SORTED_SET_KEY);
        if (taskDataString != null) {
            try {
                Map<String, Object> taskData = objectMapper.readValue(taskDataString, Map.class);
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
}
