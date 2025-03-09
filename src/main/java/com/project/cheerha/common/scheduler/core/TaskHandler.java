package com.project.cheerha.common.scheduler.core;

import java.util.Map;

public interface TaskHandler {
    String getTaskType(); //처리할 작업 유형
    void handle(Map<String, Object> payload); //작업 실행 로직

    //스케줄링 주기
    default long getScheduleIntervalMillis() {
        return 60000L;
    }

    //스케줄러에 사용하는 변수(default 는 변수 없음)
    default Map<String, Object> getDefaultPayload() {
        return null;
    }
}