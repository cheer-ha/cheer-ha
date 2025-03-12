package com.project.cheerha.common.redis.redisson;

import com.project.cheerha.common.scheduler.repository.KeyValueRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedissonKeyValueRepositoryImpl implements KeyValueRepository {

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
}
