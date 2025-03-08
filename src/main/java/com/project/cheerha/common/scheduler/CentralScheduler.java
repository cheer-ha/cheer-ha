package com.project.cheerha.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cheerha.common.util.InstanceUtil;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CentralScheduler {

    private final RedissonClient redissonClient;
    private final TaskProducer taskProducer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<TaskHandler> schedulerTaskHandlers;

    //최신 인스턴스 정보를 저장할 Redis 키
    private static final String LATEST_INSTANCE_KEY = "scheduler:latest-instance";

    @Scheduled(fixedDelay = 5000)
    public void scheduleTasks() {
        updateLatestInstance();
        //최신 인스턴스만 스케줄링 수행
        if (!isLatestInstance()) {
            log.debug("현재 인스턴스는 최신 인스턴스가 아니므로 스케줄링하지 않습니다");
            return;
        }

        for (TaskHandler handler : schedulerTaskHandlers) {
            long scheduleIntervalMillis = handler.getScheduleIntervalMillis();
            if (scheduleIntervalMillis <= 0) {
                continue;
            }

            String taskType = handler.getTaskType();
            String lockKey = "scheduler:lock:" + taskType;
            String lastTimeKey = "scheduler:lastScheduledTime:" + taskType;

            RLock lock = redissonClient.getLock(lockKey);
            try {
                //락 획득 시간을 스케줄링 간격의 절반으로 설정 (데드락 방지)
                if (lock.tryLock(Math.min(2000, scheduleIntervalMillis/2), scheduleIntervalMillis/2, TimeUnit.MILLISECONDS)) {
                    try {
                        RBucket<String> bucket = redissonClient.getBucket(lastTimeKey);
                        long lastScheduledTime;
                        String lastTimeStr = bucket.get();
                        if (lastTimeStr == null) {
                            lastScheduledTime = Instant.now().toEpochMilli() - scheduleIntervalMillis;
                        } else {
                            lastScheduledTime = Long.parseLong(lastTimeStr);
                        }

                        long now = Instant.now().toEpochMilli();
                        if (now >= lastScheduledTime + scheduleIntervalMillis) {
                            long nextScheduledTime = lastScheduledTime + scheduleIntervalMillis;
                            taskProducer.scheduleTask(taskType, handler.getDefaultPayload(), Instant.ofEpochMilli(nextScheduledTime));
                            bucket.set(String.valueOf(nextScheduledTime));
                            log.info("CentralScheduler: 스케줄링 예약 완료 - {} | 시간 - {}", taskType, Instant.ofEpochMilli(nextScheduledTime));
                        }
                    } finally {
                        lock.unlock();
                    }
                } else {
                    log.debug("CentralScheduler: 락 획득 실패 - {}", taskType);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("CentralScheduler: 락 획득 중단 - {}", taskType);
            } catch (Exception e) {
                log.error("CentralScheduler: 예기치 못한 에러 발생 - {}: {}", taskType, e.getMessage(), e);
            }
        }
    }

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

    private void updateLatestInstance() {
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
                    log.info("현재 인스턴스는 최신이 아님 (등록 안함) - 기존: {}, 현재: {}",
                            latestStartTime, currentStartTime);
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
