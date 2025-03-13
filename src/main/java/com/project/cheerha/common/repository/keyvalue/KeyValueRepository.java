package com.project.cheerha.common.repository.keyvalue;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface KeyValueRepository {

    //Key-Value
    String getValue(String key);
    Set<String> getKeys(String key);

    void setValue(String key, String value);
    void setValue(String key, String number, Duration duration);
    void setValue(String key, String value, long ttl, TimeUnit timeUnit);

    void removeValue(String key);

    long incrementValue(String key);

    void expireValue(String key, long ttl, TimeUnit timeUnit);

    Boolean hasKey(String key);

    List<String> opsForListRange(String key, long start, long end);
    void opsForListLeftPush(String key, String value);

    Set<String> opsForZSet(String key, long start, long end);
    void opsForZSetAdd(String key, String value, long score);
    Long opsForZSetCard(String key);
    Set<String> opsForZSetReverseRange(String key, long start, long end);
    void opsForZSetRemoveRange(String key, long start, long end);

}
