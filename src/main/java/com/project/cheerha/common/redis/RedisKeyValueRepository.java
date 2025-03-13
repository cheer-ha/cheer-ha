package com.project.cheerha.common.redis;

import com.project.cheerha.common.repository.keyvalue.KeyValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisKeyValueRepository implements KeyValueRepository {

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
    public Set<String> opsForZSet(String key, long start, long end){
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    @Override
    public void opsForZSetAdd(String key, String value, long score){
        redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Long opsForZSetCard(String key){
        return redisTemplate.opsForZSet().zCard(key);
    }

    @Override
    public Set<String> opsForZSetReverseRange(String key, long start, long end){
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    @Override
    public void opsForZSetRemoveRange(String key, long start, long end){
        redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    @Override
    public void removeValue(String key){
        redisTemplate.delete(key);
    }

    @Override
    public long incrementValue(String key){
        redisTemplate.opsForValue().increment(key, 1);
        return 0;
    }

    @Override
    public void expireValue(String key, long ttl, TimeUnit timeUnit){
        redisTemplate.expire(key, ttl, timeUnit);
    }

    @Override
    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    public List<String> opsForListRange(String key, long start, long end){
        return redisTemplate.opsForList().range(key, start, end);
    }

    public void opsForListLeftPush(String key, String value){
        redisTemplate.opsForList().leftPush(key, value);
    }
}
