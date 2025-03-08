package com.project.cheerha.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CentralScheduler {

    private final InstanceManager instanceManager;
    private final RedissonClient redissonClient;
    private final TaskProducer taskProducer;
    private final List<TaskHandler> schedulerTaskHandlers;

    @Scheduled(fixedDelay = 5000)
    public void scheduleTasks() {
        instanceManager.updateLatestInstance();
        //최신 인스턴스만 스케줄링 수행
        if (!instanceManager.isLatestInstance()) {
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
}
