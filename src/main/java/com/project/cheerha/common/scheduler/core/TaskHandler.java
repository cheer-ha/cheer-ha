package com.project.cheerha.common.scheduler.core;

import com.project.cheerha.common.scheduler.strategy.FixedIntervalStrategy;
import com.project.cheerha.common.scheduler.strategy.ScheduleStrategy;

import java.util.Map;

public interface TaskHandler {

    String getTaskType(); //처리할 작업 유형
    void handle(Map<String, Object> payload); //작업 실행 로직

    //스케줄링 주기(고정 주기 예약일 경우에만 구현)
    default long getScheduleIntervalMillis() {
        return 60000L;
    }

    /**
     *  예약 전략(특정 주기 예약하고 싶은 경우 new SpecificTimeStrategy(); 로 구현)
     *  예시: @Override
     *  public ScheduleStrategy getScheduleStrategy() {
     *      return new SpecificTimeStrategy(LocalTime.of(00, 00, 0)); 매일 자정에 실행
     *  }
     */
    default ScheduleStrategy getScheduleStrategy() {
        return new FixedIntervalStrategy();
    }

    //스케줄러에 사용하는 변수(default 는 변수 없음)
    default Map<String, Object> getDefaultPayload() {
        return null;
    }
}