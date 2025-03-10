package com.project.cheerha.common.scheduler.repository;

import java.util.concurrent.TimeUnit;

public interface RedissonRepository {

    //Key-Value
    String getValue(String key);
    void setValue(String key, String value);

    //Lock
    boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;
    void unlock(String lockKey);
}
