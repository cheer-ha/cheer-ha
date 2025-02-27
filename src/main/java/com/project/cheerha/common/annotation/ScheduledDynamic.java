package com.project.cheerha.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//스케줄링을 랜덤한 간격으로 실행할 수 있게 하는 커스텀 어노테이션
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledDynamic {
    int minMinutes() default 1; //최소 1분
    int maxMinutes() default 60; //최대 60분
}
