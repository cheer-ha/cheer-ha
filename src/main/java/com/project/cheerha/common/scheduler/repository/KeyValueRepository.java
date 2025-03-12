package com.project.cheerha.common.scheduler.repository;

public interface KeyValueRepository {

    //Key-Value
    String getValue(String key);
    void setValue(String key, String value);
}
