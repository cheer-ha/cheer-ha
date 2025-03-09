package com.project.cheerha.common.scheduler;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final RedissonClient redissonClient;

    @Override
    public String getValue(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    @Override
    public void setValue(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    @Override
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.tryLock(waitTime, leaseTime, unit);
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    @Override
    public void saveLastScheduledTime(String key, long time) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(String.valueOf(time));
    }

    @Override
    public long getLastScheduledTime(String key, long defaultValue) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        String lastTimeStr = bucket.get();
        return (lastTimeStr != null) ? Long.parseLong(lastTimeStr) : defaultValue;
    }

    @Override
    public void addScheduledTask(String key, String taskData, Instant scheduledTime) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(key);
        sortedSet.add(scheduledTime.toEpochMilli(), taskData);
    }

    @Override
    public String getDueTask(String key) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(key);
        long currentTime = Instant.now().toEpochMilli();
        Collection<String> dueTasks = sortedSet.valueRange(0, true, currentTime, true);

        if (dueTasks != null && !dueTasks.isEmpty()) {
            String task = dueTasks.iterator().next();
            sortedSet.remove(task);
            return task;
        }
        return null;
    }
}
