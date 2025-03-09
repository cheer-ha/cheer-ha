package com.project.cheerha.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final TaskRepository taskRepository;
    private final TaskProducer taskProducer;
    private final List<TaskHandler> schedulerTaskHandlers;

    /**
     * 5초에 한번 작업 스케줄을 확인하고, 새 작업을 스케줄링(등록)합니다.
     */
    @Scheduled(fixedDelay = 5000)
    public void scheduleTasks() {
        instanceManager.updateLatestInstance();
        if (!instanceManager.isLatestInstance()) {
            return;
        }

        for (TaskHandler handler : schedulerTaskHandlers) {
            long scheduleIntervalMillis = handler.getScheduleIntervalMillis();
            if (scheduleIntervalMillis <= 0) continue;

            String taskType = handler.getTaskType();
            String lockKey = "scheduler:lock:" + taskType;
            String lastTimeKey = "scheduler:lastScheduledTime:" + taskType;

            try {
                if (taskRepository.tryLock(lockKey, Math.min(2000, scheduleIntervalMillis / 2), scheduleIntervalMillis / 2, TimeUnit.MILLISECONDS)) {
                    try {
                        long lastScheduledTime = taskRepository.getLastScheduledTime(lastTimeKey, Instant.now().toEpochMilli() - scheduleIntervalMillis);
                        long now = Instant.now().toEpochMilli();
                        if (now >= lastScheduledTime + scheduleIntervalMillis) {
                            long nextScheduledTime = lastScheduledTime + scheduleIntervalMillis;
                            taskProducer.scheduleTask(taskType, handler.getDefaultPayload(), Instant.ofEpochMilli(nextScheduledTime));
                            taskRepository.saveLastScheduledTime(lastTimeKey, nextScheduledTime);
                        }
                    } finally {
                        taskRepository.unlock(lockKey);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
