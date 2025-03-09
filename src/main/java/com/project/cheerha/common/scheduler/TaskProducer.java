package com.project.cheerha.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskProducer {

    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<TaskHandler> handlers;

    private static final String SORTED_SET_KEY = "scheduled-tasks";

    /**
     * 실제 작업을 등록하는 메서드
     */
    public void scheduleTask(String taskType, Map<String, Object> payload, Instant scheduledTime) {
        String taskId = UUID.randomUUID().toString();

        // TaskHandler 에서 기본 payload 가져오기 (널 방지)
        if (payload == null) {
            for (TaskHandler handler : handlers) {
                if (handler.getTaskType().equals(taskType)) {
                    payload = handler.getDefaultPayload();
                    break;
                }
            }
        }

        Map<String, Object> taskData = new HashMap<>();
        taskData.put("taskId", taskId);
        taskData.put("taskType", taskType);
        taskData.put("payload", payload);

        try {
            String taskDataStr = objectMapper.writeValueAsString(taskData);
            taskRepository.addScheduledTask(SORTED_SET_KEY, taskDataStr, scheduledTime);
        } catch (Exception e) {
            log.error("태스크 등록 실패: {}", taskType, e);
        }
    }
}
