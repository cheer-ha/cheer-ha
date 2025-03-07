package com.project.cheerha.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
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

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<TaskHandler> handlers;

    private static final String SORTED_SET_KEY = "scheduled-tasks";

    /**
     * Redis Sorted Set 에 작업을 등록하는 공통 메서드
     */
    public void scheduleTask(String taskType, Map<String, Object> payload, Instant scheduledTime) {
        String taskId = UUID.randomUUID().toString();

        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(SORTED_SET_KEY);

        //TaskHandler 에서 기본 payload 가져오기 (널 방지)
        if (payload == null) {
            for (TaskHandler handler : handlers) {
                if (handler.getTaskType().equals(taskType)) {
                    payload = handler.getDefaultPayload(); //TaskHandler 가 제공하는 기본 payload 사용
                    break;
                }
            }
        }

        Map<String, Object> taskData = new HashMap<>();
        taskData.put("taskId", taskId);
        taskData.put("taskType", taskType); //작업 유형 추가
        taskData.put("payload", payload);   //실행에 필요한 데이터

        try {
            String taskDataStr = objectMapper.writeValueAsString(taskData);
            sortedSet.add(scheduledTime.toEpochMilli(), taskDataStr);
            log.info("✅ 태스크 등록 완료: {} (실행 시간: {})", taskType, scheduledTime);
        } catch (Exception e) {
            log.info("태스크 등록 실패: {}", taskType, e);
        }
    }
}
