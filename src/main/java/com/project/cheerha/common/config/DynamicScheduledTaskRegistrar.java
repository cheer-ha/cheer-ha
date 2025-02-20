package com.project.cheerha.common.config;

import com.project.cheerha.common.annotation.ScheduledDynamic;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Random;

@Configuration
public class DynamicScheduledTaskRegistrar implements BeanPostProcessor {

    private final TaskScheduler taskScheduler;
    private final Random random = new Random();

    public DynamicScheduledTaskRegistrar() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        scheduler.initialize();
        this.taskScheduler = scheduler;
    }

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

    private void scheduleDynamicTask(Object bean, Method method, ScheduledDynamic annotation) {
        Runnable task = new ScheduledMethodRunnable(bean, method);
        scheduleNextRun(task, annotation);
    }

    private void scheduleNextRun(Runnable task, ScheduledDynamic annotation) {
        long interval = getRandomInterval(annotation.minMinutes(), annotation.maxMinutes());

        taskScheduler.schedule(() -> {
            try {
                task.run();
            } finally {
                scheduleNextRun(task, annotation);
            }
        }, triggerContext -> {
            long nextExecutionTime = System.currentTimeMillis() + interval;
            return new Date(nextExecutionTime).toInstant();
        });
    }

    private long getRandomInterval(int min, int max) {
        return (random.nextInt(max - min + 1) + min) * 60 * 1000L;
    }
}
