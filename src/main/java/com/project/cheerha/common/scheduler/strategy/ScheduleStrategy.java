package com.project.cheerha.common.scheduler.strategy;

import java.time.Instant;

public interface ScheduleStrategy {

    /**
     * 마지막 실행 시간과 고정 주기를 기반으로 다음 실행 시간을 계산
     * 특정 시간 예약 방식(Specific)이면 고정 주기는 무시됨
     */
    Instant getNextExecutionTime(Instant lastExecutionTime, long scheduleIntervalMillis);
}
