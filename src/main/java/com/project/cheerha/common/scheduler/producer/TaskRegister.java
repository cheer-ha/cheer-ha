package com.project.cheerha.common.scheduler.producer;

import com.project.cheerha.common.scheduler.core.TaskHandler;
import com.project.cheerha.common.scheduler.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TaskRegister {

    private final TaskRepository taskRepository;
    private final TaskProducer taskProducer;

    /**
     * Redis 에서 작업 시간을 확인하고 등록
     */
    public void register(TaskHandler handler) {
        long scheduleIntervalMillis = handler.getScheduleIntervalMillis();
        if (scheduleIntervalMillis <= 0) return;

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
