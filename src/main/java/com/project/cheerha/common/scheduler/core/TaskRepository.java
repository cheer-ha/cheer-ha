package com.project.cheerha.common.scheduler.core;

import java.time.Instant;

public interface TaskRepository {

    //Scheduler
    void saveLastScheduledTime(String key, long time);
    long getLastScheduledTime(String key, long defaultValue);
    void addScheduledTask(String key, String taskData, Instant scheduledTime);
    String getDueTask(String key);
}
