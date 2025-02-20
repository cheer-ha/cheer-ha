package com.project.cheerha.domain.jobopening.service;

import com.project.cheerha.domain.history.service.HistoryService;
import com.project.cheerha.domain.jobopening.dto.request.ReadJobOpeningRequestDto;
import com.project.cheerha.domain.jobopening.dto.response.ReadJobOpeningResponseDto;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobOpeningService {

    private final JobOpeningRepository jobOpeningRepository;
    private final JobOpeningFindByService jobOpeningFindByService;
    private final HistoryService historyService;


    @Transactional
    public String getJobOpeningUrlAndIncreaseViewCount(Long id) {
        JobOpening jobOpening = jobOpeningFindByService.findByForUpdateViewCount(id);

        String url = jobOpening.getJobOpeningUrl();
        if (!url.startsWith("http")) {
            url = "https://" + url;
        }
        jobOpening.increaseViewCount();
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
            historyService.saveSearchTerm(userId, requestDto.getSearchTerm());
        }

        Page<ReadJobOpeningResponseDto> dtoPage = jobOpeningRepository.findAllByCondition(
            requestDto, pageable);

        return dtoPage;
    }

    /**
     * 조회수 기준으로 상위 100개의 인기 채용공고를 조회하는 메서드입니다.
     * <p>
     * 이 메서드는 `jobOpeningRepositoryQuery`를 사용하여 조회수를 내림차순으로 정렬한 후, 인기 채용공고 100개를 반환합니다.
     * <p>
     * 페이지네이션을 지원하지만, 실제로는 상위 100개만 조회하므로 페이지 크기(size)는 100으로 고정됩니다.
     *
     * @param pageable 페이지 요청 정보 (페이지 번호, 페이지 크기 등)
     * @return 조회수가 많은 상위 100개의 인기 채용공고 목록을 포함하는 페이지 객체
     */
    @Transactional(readOnly = true)
    public Page<ReadJobOpeningResponseDto> readTop100PopularJobOpenings(Pageable pageable) {
        return jobOpeningRepository.findTop100PopularJobOpenings(pageable);
    }
}
