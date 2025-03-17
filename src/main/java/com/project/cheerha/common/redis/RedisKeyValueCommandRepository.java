package com.project.cheerha.common.redis;

import com.project.cheerha.common.repository.KeyValueCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisKeyValueCommandRepository implements KeyValueCommandRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setValue(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public void setValue(String key, String value, long ttl, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, ttl, timeUnit);
    }

    @Override
    public void removeValue(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public long incrementValue(String key) {
        Long result = redisTemplate.opsForValue().increment(key, 1);
        return result != null ? result : 0;
    }

    @Override
    public void expireValue(String key, long ttl, TimeUnit timeUnit) {
        redisTemplate.expire(key, ttl, timeUnit);
    }

    @Override
    public void pushToListLeft(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public void addToZSet(String key, String value, long score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public void removeFromZSetRange(String key, long start, long end) {
        redisTemplate.opsForZSet().removeRange(key, start, end);
    }
}
