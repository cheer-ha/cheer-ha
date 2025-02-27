//package com.project.cheerha.domain.jobopening.service;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//import com.project.cheerha.domain.jobopening.entity.JobOpening;
//import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
//import com.project.cheerha.domain.viewcount.repository.JobOpeningViewCountRepository;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.atomic.AtomicInteger;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.util.ReflectionTestUtils;
//
//@Slf4j
//@SpringBootTest
//public class GetJobOpeningUrlAndIncreaseViewCountTest {
//
//    @Autowired
//    private JobOpeningService jobOpeningService;
//
//    @Autowired
//    private JobOpeningRepository jobOpeningRepository;
//
//    @Autowired
//    private JobOpeningViewCountRepository jobOpeningViewCountRepository;
//    @Autowired
//    private JobOpeningFindByService jobOpeningFindByService;
//
//    /**
//     * 테스트 시작 전, resetViewCount 메서드를 무조건 실행하도록 @BeforeEach를 사용합니다.
//     * 모든 테스트가 실행될 때 적용되는 부분입니다.
//     * JobOpeningRepository에서 모든 파일을 가져와 리스트로 뽑고, 그 중 viewCount에 해당되는 값만
//     */
//    @BeforeEach
//    public void resetViewCount() {
//        List<JobOpening> jobOpenings = jobOpeningRepository.findAll();
//        jobOpenings.forEach(jobOpening ->
//            ReflectionTestUtils.setField(jobOpening, "viewCount", 0));
//        jobOpeningRepository.saveAll(jobOpenings);
//    }
//
//    /**
//     * 테이블 분리 후 비관적 락을 집계 테이블(ViewCount)로 별도로 두었을 때의 테스트 코드입니다.
//     * 로직 리팩토링 해서 테스트 코드 새로 짜야 해요.
//     * @throws InterruptedException
//     */
//    @Test
//    @DisplayName("비관적 락 viewCount 테이블 동시성 제어 테스트")
//    void 집계테이블_viewCount_동시성_제어_테스트 () throws InterruptedException {
//        //given
//        Long jobOpeningId = jobOpeningViewCountRepository.findAllJobOpeningId()
//            .stream().findFirst()
//            .orElseThrow(() -> new RuntimeException("테스트를 위한 채용공고 ID가 없습니다."));
//
//        int totalRequests = 100;
//        int totalThreads = 10;
//
//        long startTime = System.nanoTime();
//        AtomicInteger successCount = new AtomicInteger(0);
//        AtomicInteger failureCount = new AtomicInteger(0);
//
//        CountDownLatch latch = new CountDownLatch(totalRequests);
//        ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);
//
//        //when
//        for(int i=0;i<totalRequests;i++) {
//            executorService.submit(() -> {
//                try {
//                    jobOpeningService.increaseViewCount(jobOpeningId);
//                    successCount.incrementAndGet();
//                } catch (Exception e) {
//                    failureCount.incrementAndGet();
//                    log.info("예외발생", e);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//        executorService.shutdown();
//
//        long duration = System.nanoTime() - startTime;
//        Long viewCount = jobOpeningViewCountRepository.findViewCountByJobOpeningId(jobOpeningId);
//        //then
//        log.info("총 소요시간: {}", duration / 1_000_000);
//        log.info("초기 요청 수: {}", totalRequests);
//        log.info("성공한 요청 수: {}", successCount);
//        log.info("실패한 요청 수: {}", failureCount);
//        log.info("최종 viewCount 값: {}", viewCount);
//
//        assertThat(viewCount).isEqualTo(successCount.get());
//    }
//}
