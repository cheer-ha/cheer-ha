package com.project.cheerha.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledDynamic {
    int minMinutes() default 1; //최소 1분
    int maxMinutes() default 60; //최대 60분
}
