package com.project.cheerha.common.repository;

import java.time.Duration;
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

    void incrementValue(String key);

    void expireValue(String key, long ttl, TimeUnit timeUnit);

    Boolean hasKey(String key);
}
