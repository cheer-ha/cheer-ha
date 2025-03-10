package com.project.cheerha.common.scheduler.core;

import com.project.cheerha.common.scheduler.producer.TaskRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CentralScheduler {

    private final InstanceManager instanceManager;
    private final TaskRegister taskRegister;
    private final List<TaskHandler> schedulerTaskHandlers;

    /**
     * 5초에 한번 새 작업을 스케줄링(등록)합니다.
     */
    @Scheduled(fixedDelay = 5000)
    public void scheduleTasks() {
        instanceManager.updateLatestInstance();
        if (!instanceManager.isLatestInstance()) {
            return;
        }

        for (TaskHandler handler : schedulerTaskHandlers) {
            taskRegister.register(handler);
        }
    }
}
