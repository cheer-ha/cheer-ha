package com.project.cheerha.common.repository;

import java.util.List;
import java.util.Set;

public interface KeyHashRepository {

    Set<Object> getKeys(String key);
    List<Object> multiGet(String key, List<Object> hashKeys);
    void putValue(String key, Object hashKey, Object value);
    void deleteValue(String key, Object hashKey);
}
