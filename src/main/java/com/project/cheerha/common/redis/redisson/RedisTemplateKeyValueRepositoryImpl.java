package com.project.cheerha.common.redis.redisson;

import com.project.cheerha.common.repository.KeyValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisTemplateKeyValueRepositoryImpl implements KeyValueRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Set<String> getKeys(String key){
        return redisTemplate.keys(key);
    }

    @Override
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setValue(String key, String value, Duration duration){
        redisTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public void setValue(String key, String value, long ttl, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    @Override
    public void removeValue(String key){
        redisTemplate.delete(key);
    }

    @Override
    public void incrementValue(String key){
        redisTemplate.opsForValue().increment(key, 1);
    }

    @Override
    public void expireValue(String key, long ttl, TimeUnit timeUnit){
        redisTemplate.expire(key, ttl, timeUnit);
    }

    @Override
    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }
}
