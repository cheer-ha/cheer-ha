package com.project.cheerha.common.scheduler;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public interface TaskRepository {

    //Key-Value
    String getValue(String key);
    void setValue(String key, String value);

    //Lock
    boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;
    void unlock(String lockKey);

    void saveLastScheduledTime(String key, long time);
    long getLastScheduledTime(String key, long defaultValue);
    void addScheduledTask(String key, String taskData, Instant scheduledTime);
    String getDueTask(String key);
}
