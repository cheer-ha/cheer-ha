package com.project.cheerha.common.config;

import com.project.cheerha.common.annotation.ScheduledDynamic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Random;

/**
 * 어노테이션 @ScheduledDynamic 이 달린 메서드를 직접 찾아서
 * 랜덤한 시간 간격을 두고 스케줄링 수행
 */
@Slf4j
@Configuration
public class DynamicScheduledTaskRegistrar implements BeanPostProcessor {

    private final TaskScheduler taskScheduler;
    private final Random random = new Random();

    //미리 3개의 스레드풀 생성해놓음(jobOpening 최대 3개 생성하므로)
    public DynamicScheduledTaskRegistrar() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(3);
        scheduler.initialize();
        this.taskScheduler = scheduler;
    }

    /**
     * bean 내의 메서드를 모두 뒤져서 @ScheduledDynamic 이 붙어있으면
     * 자동으로 Task 에 등록
     * @return 원래 bean
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ScheduledDynamic.class)) {
                ScheduledDynamic annotation = method.getAnnotation(ScheduledDynamic.class);
                scheduleDynamicTask(bean, method, annotation);
            }
        }
        return bean;
    }

    /**
     * - @ScheduledDynamic 이 달린 메서드를 Runnable 로 감싸서 스케줄러에게 맡김
     * @param bean 실제 메서드를 가지고 있는 빈으로 등록된 인스턴스
     * @param method 실행해야 할 메서드
     * @param annotation @ScheduledDynamic 어노테이션 정보
     */
    private void scheduleDynamicTask(Object bean, Method method, ScheduledDynamic annotation) {
        Runnable task = new ScheduledMethodRunnable(bean, method);
        scheduleNextRun(task, annotation);
    }

    /**
     * 태스크를 일정 간격으로 반복 실행하도록 예약함
     * 매번 실행 후 다시 다음 시간을 랜덤으로 설정함
     * @param task 등록된 태스크
     */
    private void scheduleNextRun(Runnable task, ScheduledDynamic annotation) {
        taskScheduler.schedule(task, triggerContext -> {
            long interval = getRandomInterval(annotation.minMinutes(), annotation.maxMinutes());
            long nextExecutionTime = System.currentTimeMillis() + interval;
            log.info("다음 스케줄링 시간 : {}", new Date(nextExecutionTime));
            return new Date(nextExecutionTime).toInstant();
        });
    }

    /**
     * 어노테이션에서 min - max 범위 내에서 무작위로 고른 minute 를 밀리초 단위로 변환함
     * @return 랜덤한 분(밀리초)
     */
    private long getRandomInterval(int min, int max) {
        return (random.nextInt(max - min + 1) + min) * 60 * 1000L;
    }
}
