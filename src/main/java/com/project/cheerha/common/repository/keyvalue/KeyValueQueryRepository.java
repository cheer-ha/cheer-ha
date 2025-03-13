package com.project.cheerha.common.repository.keyvalue;

import java.util.List;
import java.util.Set;

public interface KeyValueQueryRepository {

    //읽기 전용
    String getValue(String key);
    Set<String> getKeys(String key);
    Boolean hasKey(String key);
    List<String> getListRange(String key, long start, long end);
    Set<String> getZSetRange(String key, long start, long end);
    Long getZSetCard(String key);
    Set<String> getZSetReverseRange(String key, long start, long end);
}
