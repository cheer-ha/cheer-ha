package com.project.cheerha.domain.jobopening.service;

import com.project.cheerha.common.util.variable.IndexName;
import com.project.cheerha.domain.searchhistory.service.SearchHistoryService;
import com.project.cheerha.common.redis.RedisDistributedLockManager;
import com.project.cheerha.common.redis.RedisViewCountManager;
import com.project.cheerha.domain.jobopening.dto.request.ReadJobOpeningRequestDto;
import com.project.cheerha.domain.jobopening.dto.response.ReadJobOpeningResponseDto;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import com.project.cheerha.domain.viewcount.entity.JobOpeningViewCount;
import com.project.cheerha.domain.viewcount.repository.JobOpeningViewCountRepository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobOpeningService {

    private final JobOpeningRepository jobOpeningRepository;
    private final SearchHistoryService searchHistoryService;
    private final JobOpeningFindByService jobOpeningFindByService;
    private final RedisViewCountManager redisViewCountManager;
    private final JobOpeningViewCountRepository jobOpeningViewCountRepository;
    private final RedisDistributedLockManager redisDistributedLockManager;

    /**
     * 채용공고 리다이렉트 동시성 제어를 위한 집계 테이블 조회수 카운팅 메서드 입니다.
     * viewCount 정보를 관리하는 집계 테이블에서 조회수가 카운팅됩니다.
     * viewcount 테이블에서 비관 락이 작동하여 count 값의 정합성을 유지합니다.
     */
    @Transactional
    public String increaseViewCount(Long jobOpeningId) {
        redisViewCountManager.increaseViewCount(jobOpeningId);
        String message = updateViewCount(jobOpeningId);
        log.info("JobOpeningId: {} 레디스 조회수 :{}", jobOpeningId,redisViewCountManager.getViewCount(jobOpeningId));
        return message;
    }

    /**
     * 레디스에 있는 채용공고 조회수를 집계테이블로 업데이트 하는데 사용하는 메서드
     * @param jobOpeningId
     */
    public String updateViewCount(Long jobOpeningId) {
        String lockKey = "JOB_OPENING_VIEW_COUNT_" + jobOpeningId;
        int retryCount = 3; // 최대 3번 재시도
        boolean success = false;

        while (retryCount-- > 0)
        success = redisDistributedLockManager.tryLockAndRun(
            lockKey,
            3,
            5,
            TimeUnit.SECONDS,
            () -> {
                // 실제 처리할 비즈니스 로직
                Long redisViewCount = getRedisViewCount(jobOpeningId);
                JobOpeningViewCount jobOpeningViewCount = jobOpeningViewCountRepository.findWithLockByJobOpeningId(
                        jobOpeningId)
                    .orElseGet(() -> {
                        JobOpening jobOpening = jobOpeningFindByService.findById(jobOpeningId);
                        return jobOpeningViewCountRepository.save(
                            JobOpeningViewCount.create(jobOpening));
                    });
                jobOpeningViewCount.increaseViewCount(redisViewCount);
                redisViewCountManager.resetViewCount(jobOpeningId);
            });

        if (success) {
            // 락 획득에 성공한 케이스
            log.info("JobOpeningId: {} 락 획득 로직 실행 완료", jobOpeningId);
            return "success";
        } else {
            log.warn("JobOpeningId: {} 락 획득 실패", jobOpeningId);
            try {
                Thread.sleep(100);// 1초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log.error("JobOpeningId: {} 조회수 동기화 최종 실패 (모든 재시도 실패)", jobOpeningId);
            return "fail";
        }
    }

    public Long getRedisViewCount(Long jobOpeningId) {
        return redisViewCountManager.getViewCount(jobOpeningId);
    }

    /**
     * 페이지 리다이렉트를 위한 서비스 로직입니다.
     * @param id jobOpening의 id
     * @return 리다이렉트 될 페이지 URL
     */
    public String getJobOpeningUrl(Long id) {
        JobOpening jobOpening = jobOpeningFindByService.findById(id);
        String url = jobOpening.getJobOpeningUrl();
        if (!url.startsWith("http")) {
            url = "https://" + url;
        }
        return url;
    }

    /**
     * 채용 공고 목록을 조회하는 메서드입니다.
     *
     * requestDto에 값이 포함되면 해당 조건을 기준으로 필터링이 적용됩니다.
     * searchTerm이 존재하면 해당 검색어를 Redis에 저장합니다.
     *
     * @param requestDto 조회 조건을 포함한 요청 Dto
     * @param userId 검색어 저장을 위한 유저의 Id
     * @param pageable 페이지 요청 정보 (페이지 번호, 페이지 크기)
     * @return 필터링된 채용 공고 목록
     */
    @Transactional
    public Page<ReadJobOpeningResponseDto> readJobOpenings(
            ReadJobOpeningRequestDto requestDto,
            Long userId,
            Pageable pageable
    ) {
        if (requestDto.getSearchTerm() != null) {
            searchHistoryService.saveSearchTerm(userId, requestDto.getSearchTerm());
        }
        Page<ReadJobOpeningResponseDto> dtoPage = jobOpeningRepository.findAllByCondition(
                requestDto, pageable);

        // 마감된 채용공고를 제외하도록 필터링
        dtoPage = filterExpiredJobOpenings(dtoPage);

        return dtoPage;
    }

    /**
     * 조회수 기준으로 상위 100개의 인기 채용공고를 조회하는 메서드입니다.
     *
     * 이 메서드는 `jobOpeningRepositoryQuery`를 사용하여 조회수를 내림차순으로 정렬한 후,
     * 인기 채용공고 100개를 반환합니다.
     *
     * 페이지네이션을 지원하지만, 실제로는 상위 100개만 조회하므로 페이지 크기(size)는 100으로 고정됩니다.
     *
     * @param pageable 페이지 요청 정보 (페이지 번호, 페이지 크기 등)
     * @return 조회수가 많은 상위 100개의 인기 채용공고 목록을 포함하는 페이지 객체
     */
    @Transactional(readOnly = true)
    public Page<ReadJobOpeningResponseDto> readTop100PopularJobOpenings(Pageable pageable) {
        Pageable adjustedPageable = adjustPageable(pageable);
        // 인기 채용공고 조회
        Page<ReadJobOpeningResponseDto> dtoPage = jobOpeningRepository.findTop100PopularJobOpenings(adjustedPageable);

        // 마감된 채용공고를 제외하도록 필터링
        dtoPage = filterExpiredJobOpenings(dtoPage);

        // 조회된 채용공고 DTO에 requiredSkills 추가
        for (ReadJobOpeningResponseDto dto : dtoPage) {
            JobOpening jobOpening = jobOpeningFindByService.findById(dto.getId());
            if (jobOpening != null) {
                // JobOpening에서 requiredSkills를 가져오기
                List<String> requiredSkillList = jobOpening.getRequiredSkillList();
                dto.addRequiredSkills(requiredSkillList); // DTO에 requiredSkills 추가
            }
        }

        return dtoPage;
    }

    /**
     * 마감된 채용공고를 제외하는 메서드
     */
    private Page<ReadJobOpeningResponseDto> filterExpiredJobOpenings(Page<ReadJobOpeningResponseDto> dtoPage) {
        // 마감된 채용공고를 제외하는 로직
        ZonedDateTime now = ZonedDateTime.now();
        List<ReadJobOpeningResponseDto> filteredDtoList = dtoPage.getContent().stream()
                .filter(dto -> dto.getHiringEndAt().isAfter(now))
                .collect(Collectors.toList());

        return new PageImpl<>(filteredDtoList, dtoPage.getPageable(), dtoPage.getTotalElements());
    }

    /**
     * 페이지 번호와 페이지 크기를 조정하여 유효한 값으로 반환하는 메서드입니다.
     *
     * 이 메서드는 주어진 페이지 번호(pageable.getPageNumber())와 페이지 크기(pageable.getPageSize())를 기준으로,
     * 총 페이지 수를 넘지 않도록 페이지 번호를 조정합니다. 또한, 페이지 크기는 최대값인 `MAX_POPULAR_SIZE`를 기준으로 설정됩니다.
     * 만약 사용자가 요청한 페이지 번호가 전체 페이지 수를 초과하는 경우, 마지막 페이지로 자동 조정됩니다.
     *
     * @param pageable 페이징 처리 정보 (페이지 번호와 페이지 크기)
     * @return 유효한 페이지 번호와 페이지 크기를 기준으로 설정된 PageRequest 객체
     */
    private Pageable adjustPageable(Pageable pageable) {
        int pageSize = Math.min(pageable.getPageSize(), IndexName.MAX_POPULAR_SIZE);
        int pageNumber = pageable.getPageNumber();

        // 총 페이지 수가 초과되지 않도록 처리
        int totalPages = (int) Math.ceil((double) IndexName.MAX_JOB_OPENING_SIZE / pageSize); // totalElements = 100
        if (pageNumber >= totalPages) {
            pageNumber = totalPages - 1;  // 페이지 번호가 totalPages보다 크면 마지막 페이지로 설정
        }

        // PageRequest를 사용하여 페이지 번호와 페이지 크기 설정
        return PageRequest.of(pageNumber, pageSize);
    }
}
