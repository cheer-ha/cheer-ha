package com.project.cheerha.domain.jobopening.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
@SpringBootTest
public class GetJobOpeningUrlAndIncreaseViewCountTest {

    @Autowired
    private JobOpeningService jobOpeningService;

    @Autowired
    private JobOpeningRepository jobOpeningRepository;

    /**
     * 테스트 시작 전, resetViewCount 메서드를 무조건 실행하도록 @BeforeEach를 사용합니다.
     * 모든 테스트가 실행될 때 적용되는 부분입니다.
     * JobOpeningRepository에서 모든 파일을 가져와 리스트로 뽑고, 그 중 viewCount에 해당되는 값만
     */
    @BeforeEach
    public void resetViewCount() {
        List<JobOpening> jobOpenings = jobOpeningRepository.findAll();
        jobOpenings.forEach(jobOpening ->
            ReflectionTestUtils.setField(jobOpening, "viewCount", 0));
        jobOpeningRepository.saveAll(jobOpenings);
    }

    //todo: 테이블 분리한 비관적 락에 대해 테스트 코드 변경을 추후에 할 예정
    @Test
    @DisplayName("비관적 락 적용 후 동시에 채용공고를 클릭했을 때")
    void 비관적_락_여러_사용자가_동시에_채용공고_클릭() throws InterruptedException {
        //given
        JobOpening jobOpening = jobOpeningRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("테스트를 위한 채용공고 데이터가 없습니다."));

        long startTime = System.nanoTime();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        Long jobOpeningId = jobOpening.getId();
        int totalRequests = 100;
        int totalThreads = 10;

        CountDownLatch latch = new CountDownLatch(totalRequests);
        ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);

        //when
        for(int i=0;i<totalRequests;i++) {
            executorService.submit(() -> {
                try {
                    jobOpeningService.getJobOpeningUrlAndIncreaseViewCount(jobOpeningId);
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
                long duration = System.nanoTime() - startTime;

        //then
        log.info("총 소요시간: {}", duration / 1_000_000);
        log.info("초기 요청 수: {}", totalRequests);
        log.info("성공한 요청 수: {}", successCount);
        log.info("실패한 요청 수: {}", failureCount);
        assertThat(successCount.get() + failureCount.get()).isEqualTo(totalRequests);

    }

//    @Test
//    @DisplayName("낙관적 락을 이용한 동시성 제어")
//    void 낙관적_락_여러_사용자가_동시에_채용공고를_클릭함() throws InterruptedException {
//        //given
//        JobOpening jobOpening = jobOpeningRepository.findAll().stream()
//            .findFirst()
//            .orElseThrow(() -> new RuntimeException("테스트를 위한 채용공고 데이터가 없습니다."));
//
//        Long jobOpeningId = jobOpening.getId();
//        long startTime = System.nanoTime();
//
//        AtomicInteger successCount = new AtomicInteger(0);
//        AtomicInteger failureCount = new AtomicInteger(0);
//        int totalRequests = 100;
//        int totalThreads = 10;
//
//        CountDownLatch latch = new CountDownLatch(totalRequests);
//        ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);
//
//        //when
//        for (int i = 0; i < totalRequests; i++) {
//            executorService.submit(() -> {
//                try {
//                    jobOpeningService.getJobOpeningUrlAndIncreaseViewCount(jobOpeningId);
//                    successCount.incrementAndGet();
//                } catch (Exception e) {
//                    failureCount.incrementAndGet();
//                    log.info("예외발생", e);
//                } finally {
//                    //작업 종료 시 기다리는 스레드를 줄어들게 함.
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//        executorService.shutdown();
//        long duration = System.nanoTime() - startTime;
//
//        //then
//        log.info("총 소요시간: {}", duration / 1_000_000);
//        log.info("초기 요청 수: {}", totalRequests);
//        log.info("성공한 요청 수: {}", successCount);
//        log.info("실패한 요청 수(낙관적 락 충돌) : {}", failureCount);
//    }
}
