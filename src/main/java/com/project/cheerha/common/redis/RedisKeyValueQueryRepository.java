package com.project.cheerha.common.redis;

import com.project.cheerha.common.repository.KeyValueQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RedisKeyValueQueryRepository implements KeyValueQueryRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Set<String> getKeys(String key) {
        return redisTemplate.keys(key);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public List<String> getListRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public Set<String> getZSetRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    @Override
    public Long getZSetCard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    @Override
    public Set<String> getZSetReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }
}
