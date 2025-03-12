package com.project.cheerha.common.scheduler.repository;

import java.util.concurrent.TimeUnit;

public interface LockRepository {

    //Lock
    boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;
    void unlock(String lockKey);
}
