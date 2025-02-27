package com.project.cheerha.domain.jobopening.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import com.project.cheerha.domain.viewcount.entity.JobOpeningViewCount;
import com.project.cheerha.domain.viewcount.repository.JobOpeningViewCountRepository;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
@SpringBootTest
public class GetJobOpeningUrlAndIncreaseViewCountIntegrationTest {

    @Autowired
    private JobOpeningService jobOpeningService;

    @Autowired
    private JobOpeningViewCountRepository jobOpeningViewCountRepository;

    @Autowired
    private JobOpeningFindByService jobOpeningFindByService;

    @Autowired
    private JobOpeningRepository jobOpeningRepository;

    @BeforeEach
    public void resetViewCount() {
        List<JobOpeningViewCount> viewCounts = jobOpeningViewCountRepository.findAll();
        viewCounts.forEach(jobOpening ->
            ReflectionTestUtils.setField(jobOpening, "viewCount", 0));
        jobOpeningViewCountRepository.saveAll(viewCounts);
    }

    /**
     * 테이블 분리 후 비관적 락을 집계 테이블(ViewCount)로 별도로 두었을 때의 테스트 코드입니다. 집계테이블의 비관적 락 적용 후 테스트입니다.
     *
     * @throws InterruptedException
     */
    @Test
    @DisplayName("비관적 락 viewCount 테이블 동시성 제어 통합 테스트")
    void 집계테이블_viewCount_동시성_제어_통합테스트() throws InterruptedException {
        //given
        JobOpening jobOpening = jobOpeningRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("테스트를 위한 채용공고 데이터가 없습니다."));

        Long jobOpeningId = jobOpening.getId();
        int totalRequests = 100;
        int totalThreads = 10;

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        CountDownLatch latch = new CountDownLatch(totalRequests);
        ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);

        //when
        for (int i = 0; i < totalRequests; i++) {
            executorService.submit(() -> {
                try {
                    jobOpeningService.increaseViewCount(jobOpeningId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    log.info("예외발생", e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        //then
        JobOpeningViewCount finalViewCount = jobOpeningViewCountRepository.findByJobOpeningId(
                jobOpeningId)
            .orElseThrow(() -> new IllegalArgumentException("테스트에 사용할 집계테이블이 존재하지 않습니다."));
        log.info("초기 요청 수: {}", totalRequests);
        log.info("성공한 요청 수: {}", successCount);
        log.info("실패한 요청 수: {}", failureCount);
        log.info("최종 viewCount 값: {}", finalViewCount.getViewCount());

        assertThat(successCount.get()).isEqualTo(totalRequests);
        assertThat(finalViewCount.getViewCount()).isEqualTo(totalRequests);
    }

    @Test
    @DisplayName("비관적 락 테이블 분리 후 조회속도 테스트")
    void 동시에_조회를_클릭 () throws InterruptedException {
        JobOpening jobOpening = jobOpeningRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("테스트를 위한 채용공고 데이터가 없습니다."));

        Long jobOpeningId = jobOpening.getId();
        int totalRequests = 100;
        int totalThreads = 10;

        long startTime = System.nanoTime();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        CountDownLatch latch = new CountDownLatch(totalRequests);
        ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);

        //when
        for (int i = 0; i < totalRequests; i++) {
            executorService.submit(() -> {
                try {
                    jobOpeningFindByService.findById(jobOpeningId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    log.info("예외발생", e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long duration = System.nanoTime() - startTime;
        //then

        log.info("총 소요시간: {}", duration / 1_000_000);
        log.info("초기 요청 수: {}", totalRequests);
        log.info("성공한 요청 수: {}", successCount);
        log.info("실패한 요청 수: {}", failureCount);

        assertThat(successCount.get()).isEqualTo(totalRequests);
    }
}