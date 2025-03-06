package com.project.cheerha.common.redis;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisDistributedLockManager {

    /**
     * todo:아직 락 적용 전, 레디스만 적용해 보는 중
     */
    private final RedissonClient redissonClient;

    public boolean tryLockAndRun(String lockKey,long waitTime, long leaseTime, TimeUnit unit, Runnable task) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;

        try {
            // tryLockAndRun() 결과를 isLocked에 저장
            isLocked = lock.tryLock(waitTime, leaseTime, unit);
            if(isLocked) {
                task.run(); // Runnable은 반환값이 없이 run()만 실행
                return true;
            }
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            if (isLocked) {
                try {
                    lock.unlock(); // 락 해제
                } catch (IllegalMonitorStateException e) {
                    log.info("락 해제 중 예외 발생: {}", e.getMessage());
                }
            }
        }
    }
}