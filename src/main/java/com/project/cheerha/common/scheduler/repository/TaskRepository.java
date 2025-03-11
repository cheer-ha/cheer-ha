package com.project.cheerha.common.scheduler.repository;

import java.time.Instant;

public interface TaskRepository {

    //Scheduler
    void saveLastScheduledTime(String key, long time);
    long getLastScheduledTime(String key, long defaultValue);
    void addScheduledTask(String key, String taskData, Instant scheduledTime);
    String getDueTask(String key);
    void removeExpiredBuckets(String key);
    void removeExpiredTasks(String key);
}
