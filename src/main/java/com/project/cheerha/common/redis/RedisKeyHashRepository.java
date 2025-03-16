package com.project.cheerha.common.redis;

import com.project.cheerha.common.repository.KeyHashRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RedisKeyHashRepository implements KeyHashRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Set<Object> getKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    @Override
    public List<Object> multiGet(String key, List<Object> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    @Override
    public void putValue(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void deleteValue(String key, Object hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }
}
