package com.project.cheerha.common.scheduler;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final RedissonClient redissonClient;
    private final Map<String, RLock> lockMap = new ConcurrentHashMap<>();

    @Override
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
        RLock lock = redissonClient.getLock(lockKey);
        boolean acquired = lock.tryLock(waitTime, leaseTime, timeUnit);
        if (acquired) {
            lockMap.put(lockKey, lock);
        }
        return acquired;
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = lockMap.remove(lockKey);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

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
    public void addToSortedSet(String key, long score, String value) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(key);
        sortedSet.add(score, value);
    }

    @Override
    public Collection<String> getRangeFromSortedSet(String key, long minScore, long maxScore) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(key);
        return sortedSet.valueRange(minScore, true, maxScore, true);
    }

    @Override
    public void removeFromSortedSet(String key, String value) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(key);
        sortedSet.remove(value);
    }
}
