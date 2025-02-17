package com.project.cheerha.domain.jobOpening.service;

import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import com.project.cheerha.domain.jobOpening.repository.JobOpeningRepository;
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

    //테스트 수행 전 초기화로 변경
    @BeforeEach
    public void resetViewCount() {
        List<JobOpening> jobOpenings = jobOpeningRepository.findAll();
        jobOpenings.forEach(jobOpening ->
            ReflectionTestUtils.setField(jobOpening, "viewCount", 0));
        jobOpeningRepository.saveAll(jobOpenings);
    }

    @Test
    @DisplayName("채용공고를 동시에 클릭했을 때")
    void 채용공고_동시성_문제_테스트() throws InterruptedException {
        //given
        JobOpening jobOpening = jobOpeningRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("테스트를 위한 채용공고 데이터가 없습니다."));

        Long jobOpeningId = jobOpening.getId();
        long startTime = System.nanoTime();

        AtomicInteger successCount = new AtomicInteger(0);
        int totalRequests = 100;
        int totalThreads = 10;

        CountDownLatch latch = new CountDownLatch(totalRequests);
        ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);

        //when
        for (int i = 0; i < totalRequests; i++) {
            executorService.submit(() -> {
                try {
                    jobOpeningService.getJobOpeningUrlAndIncreaseViewCount(jobOpeningId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    log.info("예외발생", e);
                } finally {
                    //작업 종료 시 기다리는 스레드를 줄어들게 함.
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
        long duration = System.nanoTime() - startTime;

        //then
        JobOpening updateViewCount = jobOpeningRepository.findById(jobOpeningId)
            .orElseThrow(() -> new RuntimeException("채용공고 데이터가 없습니다."));

        log.info("총 소요시간: {}", duration / 1_000_000);
        log.info("초기 요청 수: {}", totalRequests);
        log.info("성공한 요청 총 개수: {}", updateViewCount.getViewCount());
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
//        AtomicInteger retry = new AtomicInteger(0);
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

    }

}
