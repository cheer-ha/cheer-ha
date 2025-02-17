package com.project.cheerha.domain.jobOpening.service;

import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import com.project.cheerha.domain.jobOpening.repository.JobOpeningRepository;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
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

    //테스트 수행 후 viewCount를 초기화하기 위한 reset메서드
    @AfterEach
    public void resetViewCount() {
        List<JobOpening> jobOpenings = jobOpeningRepository.findAll();
        jobOpenings.forEach(jobOpening -> ReflectionTestUtils.setField(jobOpening, "viewCount", 0));
        jobOpeningRepository.saveAll(jobOpenings);
    }

    @Test
    @DisplayName("낙관적 락을 이용한 동시성 제어")
    void 여러_사용자가_동시에_채용공고를_클릭함 () {
        /**
         * 이미 더미데이터 생성을 위한 코드가 존재하므로 해당코드 실행 후
         * 실제 DB에 적용된 더미데이터로 동시성 제어
         */
        //given
        JobOpening jobOpening = jobOpeningRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("테스트를 위한 채용공고 데이터가 없습니다."));

        Long jobOpeningId = jobOpening.getId();

        //낙관적 락 테스트이므로 충돌 가능성은 낮게 조정하여 테스트
        int totalRequests = 10;
        int totalThreads = 10;

        CountDownLatch latch = new CountDownLatch(totalRequests);
        ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);

        //when
        for(int i=0;i<totalRequests;i++) {
            executorService.submit(() -> {
                try {
                jobOpeningService.getJobOpeningUrlAndIncreaseViewCount(jobOpeningId);
            } catch (Exception e) {
                    throw new RuntimeException(e);
            } finally {
                    //작업 종료 시 기다리는 스레드를 줄어들게 함.
                    latch.countDown();
                }
            });
        }

        try {
          latch.await();
        } catch (InterruptedException e) {
            log.info("테스트 중 인터럽트 예외 발생");
            Thread.currentThread().interrupt();
        }
        executorService.shutdown();

        JobOpening updateViewCount = jobOpeningRepository.findById(jobOpeningId)
            .orElseThrow(() -> new RuntimeException("채용공고 데이터가 없습니다."));

        log.info("초기 요청 수: {}", totalRequests);
        log.info("성공한 요청 총 개수: {}", updateViewCount.getViewCount());
    }
}
