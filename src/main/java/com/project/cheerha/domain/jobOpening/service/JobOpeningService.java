package com.project.cheerha.domain.jobOpening.service;

import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.domain.history.entity.History;
import com.project.cheerha.domain.history.repository.HistoryRepository;
import com.project.cheerha.domain.jobOpening.dto.request.ReadJobOpeningRequestDto;
import com.project.cheerha.domain.jobOpening.dto.response.ReadJobOpeningResponseDto;
import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import com.project.cheerha.domain.jobOpening.repository.JobOpeningRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class  JobOpeningService {

    private final JobOpeningRepository jobOpeningRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final UserFindByService userFindByIdService;
    private final JobOpeningFindByService jobOpeningFindByService;


        @Transactional
        public String getJobOpeningUrlAndIncreaseViewCount(Long id) {
            JobOpening jobOpening = jobOpeningRepository.findByForUpdateViewCount(id).orElseThrow(
                () -> new NotFoundException(DataErrorCode.JOB_OPENING_NOT_FOUND)
            );

        String url = jobOpening.getJobOpeningUrl();
        if (!url.startsWith("http")) {
            url = "https://" + url;
        }
        increaseViewCountRetry(jobOpening.getId(), jobOpening.getVersion());

        return url;
    }


//    public void increaseViewCountRetry(Long id, Long version) {
//        for (int i = 0; i < 5; i++) {
//            int updatedRows = jobOpeningRepository.updateViewCountWithOptimisticLock(id, version);
//            log.info("업데이트 된 행 개수 : {}", updatedRows);
//            if (updatedRows > 0) {
//                return;
//            } else {
//                log.warn("낙관적 락 충돌로 인한 재시도 횟수 {}회", i + 1);
//            }
//            throw new RuntimeException("충돌 횟수 초과");
//        }
//    }


    @Transactional
    public Page<ReadJobOpeningResponseDto> readJobOpenings(
        ReadJobOpeningRequestDto requestDto,
        Long userId,
        Pageable pageable
    ) {
        User user = userFindByIdService.findById(userId);

        if (requestDto.getSearchTerm() != null) {
            History history = History.toEntity(user, requestDto.getSearchTerm());
            historyRepository.save(history);
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
