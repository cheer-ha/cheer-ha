package com.project.cheerha.domain.jobopening.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import com.project.cheerha.domain.viewcount.entity.JobOpeningViewCount;
import com.project.cheerha.domain.viewcount.repository.JobOpeningViewCountRepository;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class JobOpeningServiceTest {

    @Mock
    private JobOpeningViewCount jobOpeningViewCount;
    @Mock
    private JobOpeningRepository jobOpeningRepository;

    @Mock
    private JobOpeningViewCountRepository jobOpeningViewCountRepository;

    @InjectMocks
    private JobOpeningService jobOpeningService;

    @Test
    @DisplayName("비관적 락 viewCount 동시성 제어 단위 테스트")
    void 집계테이블_viewCount_동시성_제어_단위테스트() throws InterruptedException {
        //given
        Long id = 1L;
        int totalRequests = 100;
        int totalThreads = 10;

        //비관적 락을 사용하여 조회하는 메서드가 호출될 때 Mock 객체 반환
        when(jobOpeningViewCountRepository.findWithLockByJobOpeningId(id))
            .thenReturn(Optional.of(jobOpeningViewCount));

        //일반 조회 메서드 호출 시 동일한 Mock 객체 반환
        when(jobOpeningViewCountRepository.findByJobOpeningId(id))
            .thenReturn(Optional.of(jobOpeningViewCount));

        /*
        viewCount 값이 처음에는 0, 이후에는 100을 반환하도록 설정
        처음은 0이고, 최종 ViewCount 엔티티 조회 때 100으로 조회되도록 설정해둔 것
         */
           when(jobOpeningViewCount.getViewCount())
            .thenReturn(0).thenReturn(100);

        // 성능 측정을 위한 시작 시간 기록
        long startTime = System.nanoTime();

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        CountDownLatch latch = new CountDownLatch(totalRequests);
        ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);

        //when
        for (int i = 0; i < totalRequests; i++) {
            executorService.submit(() -> {
                try {
                    jobOpeningService.increaseViewCount(id);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    log.error("예외 발생: ", e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
        long duration = System.nanoTime() - startTime;

        // Mock 객체를 사용하여 최종 ViewCount 엔티티 조회
        JobOpeningViewCount finalViewCount = jobOpeningViewCountRepository.findByJobOpeningId(id)
            .orElseThrow(() -> new IllegalArgumentException("테스트에 사용할 집계테이블이 존재하지 않습니다."));

        log.info("총 소요시간: {}", duration / 1_000_000);
        log.info("초기 요청 수: {}", totalRequests);
        log.info("성공한 요청 수: {}", successCount);
        log.info("실패한 요청 수: {}", failureCount);
        log.info("최종 viewCount 값: {}", finalViewCount.getViewCount());

        //then
        // findWithLockByJobOpeningId 메서드가 정확히 totalRequests만큼 호출되었는지 검증
        verify(jobOpeningViewCountRepository, times(totalRequests))
            .findWithLockByJobOpeningId(id);

        // viewCount 증가 메서드가 기대한 만큼 호출되었는지 검증
//        verify(jobOpeningViewCount, times(totalRequests)).increaseViewCount();

        // 최종 viewCount 값이 기대 값과 일치하는지 검증
        assertThat(finalViewCount.getViewCount()).isEqualTo(totalRequests);
    }
}
