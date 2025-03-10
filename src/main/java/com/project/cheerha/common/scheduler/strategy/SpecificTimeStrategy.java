package com.project.cheerha.common.scheduler.strategy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class SpecificTimeStrategy implements ScheduleStrategy {

    private final LocalTime specificTime;

    /**
     * @param specificTime 매일 실행할 특정 시간
     */
    public SpecificTimeStrategy(LocalTime specificTime) {
        this.specificTime = specificTime;
    }

    @Override
    public Instant getNextExecutionTime(Instant lastExecutionTime, long scheduleIntervalMillis) {
        ZoneId zone = ZoneId.of("Asia/Seoul");
        LocalDateTime now = LocalDateTime.now(zone);
        LocalDateTime candidate = LocalDateTime.of(now.toLocalDate(), specificTime);
        if (candidate.isBefore(now) || candidate.isEqual(now)) {
            candidate = candidate.plusDays(1);
        }
        return candidate.atZone(zone).toInstant();
    }

}
