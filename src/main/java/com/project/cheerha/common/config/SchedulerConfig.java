package com.project.cheerha.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    // 이메일 전송 스케줄러에 맞는 스레드 풀 생성
    @Bean
    public ThreadPoolTaskScheduler emailTaskScheduler() {
        // 여러 작업을 병렬로 처리할 수 있도록 스레드 풀을 관리하는 클래스
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        // 스레드 풀의 크기 설정
        // 최대 10개의 스레드를 사용하여 동시에 10개의 이메일 전송 작업을 처리하도록 설정
        scheduler.setPoolSize(10);

        // 스레드 이름 접두사를 설정하여 각 스레드 구분
        // 특수 기호를 사용하여 검색 시 쉽게 찾도록 함
        scheduler.setThreadNamePrefix("$$$-Email-Scheduler-thread-");

        // 설정이 끝난 스케줄러 반환
        return scheduler;
    }
}
