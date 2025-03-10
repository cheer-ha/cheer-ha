package com.project.cheerha.common.scheduler.strategy;

import java.time.Instant;

public class FixedIntervalStrategy implements ScheduleStrategy {

    @Override
    public Instant getNextExecutionTime(Instant lastExecutionTime, long scheduleIntervalMillis) {
        return lastExecutionTime.plusMillis(scheduleIntervalMillis);
    }
}
