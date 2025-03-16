package com.project.cheerha.common.repository;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public interface KeyValueCommandRepository {

    //쓰기 전용
    void setValue(String key, String value);
    void setValue(String key, String value, Duration duration);
    void setValue(String key, String value, long ttl, TimeUnit timeUnit);
    void removeValue(String key);
    long incrementValue(String key);
    void expireValue(String key, long ttl, TimeUnit timeUnit);
    void pushToListLeft(String key, String value);
    void addToZSet(String key, String value, long score);
    void removeFromZSetRange(String key, long start, long end);
}
