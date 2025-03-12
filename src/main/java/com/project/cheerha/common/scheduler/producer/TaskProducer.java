package com.project.cheerha.common.scheduler.producer;

import com.project.cheerha.common.scheduler.core.TaskHandler;
import com.project.cheerha.common.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskProducer {

    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<TaskHandler> handlers;

    private static final String SORTED_SET_KEY = "scheduled-tasks";

    /**
     * TaskHandler 에서 작업을 가져와 등록하는 메서드
     */
    public void scheduleTask(String taskType, Map<String, Object> payload, Instant scheduledTime) {
        //TaskHandler 에서 기본 payload 가져오기 (널 방지)
        if (payload == null) {
            for (TaskHandler handler : handlers) {
                if (handler.getTaskType().equals(taskType)) {
                    payload = handler.getDefaultPayload();
                    break;
                }
            }
        }

        Map<String, Object> taskData = new HashMap<>();
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
