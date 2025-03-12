package com.project.cheerha.common.scheduler.producer;

import com.project.cheerha.common.scheduler.repository.LockRepository;
import com.project.cheerha.common.scheduler.core.TaskHandler;
import com.project.cheerha.common.scheduler.repository.TaskRepository;
import com.project.cheerha.common.scheduler.strategy.FixedIntervalStrategy;
import com.project.cheerha.common.scheduler.strategy.ScheduleStrategy;
import com.project.cheerha.common.scheduler.strategy.SpecificTimeStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TaskRegister {

    private final LockRepository lockRepository;
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
            if (lockRepository.tryLock(lockKey, Math.min(2000, scheduleIntervalMillis / 2), scheduleIntervalMillis / 2, TimeUnit.MILLISECONDS)) {
                try {
                    ScheduleStrategy strategy = handler.getScheduleStrategy();
                    Instant now = Instant.now();

                    if (strategy instanceof SpecificTimeStrategy) {
                        //특정 시간 전략
                        Instant nextExecutionTime = strategy.getNextExecutionTime(now, scheduleIntervalMillis);
                        taskProducer.scheduleTask(taskType, handler.getDefaultPayload(), nextExecutionTime);
                        taskRepository.saveLastScheduledTime(lastTimeKey, nextExecutionTime.toEpochMilli());
                    } else if(strategy instanceof FixedIntervalStrategy){
                        //고정 주기 전략
                        long defaultLastTime = now.minusMillis(scheduleIntervalMillis).toEpochMilli();
                        long lastScheduledTimeMillis = taskRepository.getLastScheduledTime(lastTimeKey, defaultLastTime);
                        Instant lastScheduledTime = Instant.ofEpochMilli(lastScheduledTimeMillis);
                        Instant nextExecutionTime = strategy.getNextExecutionTime(lastScheduledTime, scheduleIntervalMillis);
                        if (!now.isBefore(nextExecutionTime)) {
                            taskProducer.scheduleTask(taskType, handler.getDefaultPayload(), nextExecutionTime);
                            taskRepository.saveLastScheduledTime(lastTimeKey, nextExecutionTime.toEpochMilli());
                        }
                    }
                } finally {
                    lockRepository.unlock(lockKey);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
