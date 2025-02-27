package com.project.cheerha.utils;

import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

@Slf4j
public class TestUtils {

    public static <T> T spy(Class<T> clazz, Map<String, Object> fieldValues) {
        T entity = createEntity(clazz, fieldValues);
        return Mockito.spy(entity); //자동으로 spy() 적용
    }

    private static <T> T createEntity(Class<T> clazz, Map<String, Object> fieldValues) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                try {
                    ReflectionTestUtils.setField(instance, entry.getKey(), entry.getValue());
                } catch (IllegalArgumentException e) {
                    log.info("⚠️ Warning: Field '{}' not found in class {}", entry.getKey(), clazz.getName());
                }
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("객체 생성 실패", e);
        }
    }
}