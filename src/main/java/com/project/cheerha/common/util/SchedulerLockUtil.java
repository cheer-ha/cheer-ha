package com.project.cheerha.common.util;

import com.project.cheerha.common.exception.server.IllegalStatusException;
import com.project.cheerha.common.exception.server.ServerErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerLockUtil {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String LOCK_VALUE = InstanceUtil.getInstanceId();

    /**
     * 인스턴스 단위로 스케줄링을 관리하는 유틸메서드입니다.
     * key 와 ttl 을 인자로 받아 redis 에서 관리합니다.
     */
    public void lock(String keyName) {
        //setIfAbsent 로 원자적으로 락을 잡으려 시도
        Boolean acquired = redisTemplate
                .opsForValue()
                .setIfAbsent(keyName, LOCK_VALUE, 5, TimeUnit.MINUTES);

        //락을 못 잡았다면 내 인스턴스의 락인지 확인
        if (Boolean.FALSE.equals(acquired)) {
            String currentValue = redisTemplate.opsForValue().get(keyName);
            //다른 인스턴스의 소유라면 그냥 리턴
            if (!LOCK_VALUE.equals(currentValue)) {
                log.info("다른 인스턴스에서 실행중인 스케줄러입니다.");
                throw new IllegalStatusException(ServerErrorCode.ALREADY_RUNNING_SCHEDULER);
            }
            //만약 내 인스턴스라면 이전 스케줄이 남긴 락이거나 ttl 이 안 끝난 상황일 수 있으므로 ttl 재갱신
            redisTemplate.expire(keyName, 5, TimeUnit.MINUTES);
        }
    }
}
