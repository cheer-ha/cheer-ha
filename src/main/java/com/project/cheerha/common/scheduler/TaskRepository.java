package com.project.cheerha.common.scheduler;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public interface TaskRepository {

    // Lock
    boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException;
    void unlock(String lockKey);

    // Key-Value
    String getValue(String key);
    void setValue(String key, String value);

    // Sorted Set
    void addToSortedSet(String key, long score, String value);
    Collection<String> getRangeFromSortedSet(String key, long minScore, long maxScore);
    void removeFromSortedSet(String key, String value);
}
