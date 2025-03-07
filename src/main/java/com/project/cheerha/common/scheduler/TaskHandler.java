package com.project.cheerha.common.scheduler;

import java.util.Map;

public interface TaskHandler {
    String getTaskType(); //처리할 작업 유형
    void handle(Map<String, Object> payload); //작업 실행 로직

    default long getScheduleIntervalMillis() {
        return 60000L;
    }

    //기본 payload 를 제공하는 메서드, 없으면 null 리턴
    default Map<String, Object> getDefaultPayload() {
        return null;
    }
}