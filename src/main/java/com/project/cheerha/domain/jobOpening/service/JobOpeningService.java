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

        @Transactional
        public String getJobOpeningUrlAndIncreaseViewCount(Long id) {
            JobOpening jobOpening = jobOpeningRepository.findByForUpdateViewCount(id).orElseThrow(
                () -> new NotFoundException(DataErrorCode.JOB_OPENING_NOT_FOUND)
            );

            String url = jobOpening.getJobOpeningUrl();

            if (!url.startsWith("http")) {
                url = "https://" + url;
            }
            log.info("최종 리다이렉트 URL: {}", url);
            jobOpening.increaseViewCount();

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
    public Page<ReadJobOpeningResponseDto> readData(
            ReadJobOpeningRequestDto requestDto,
            Long userId,
            Pageable pageable
    ) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(DataErrorCode.USER_NOT_FOUND));

        if (requestDto.getSearchTerm() != null) {
            History history = History.toEntity(user, requestDto.getSearchTerm());
            historyRepository.save(history);
        }

        Page<ReadJobOpeningResponseDto> dtoPage = jobOpeningRepository.findAllByCondition(
                requestDto, pageable);

        return dtoPage;
    }
}
