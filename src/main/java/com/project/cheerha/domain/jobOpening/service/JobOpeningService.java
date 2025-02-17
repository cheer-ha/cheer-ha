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

    public String getJobOpeningUrlAndIncreaseViewCount(Long id) {
        JobOpening jobOpening = jobOpeningRepository.findById(id).orElseThrow(
            () -> new NotFoundException(DataErrorCode.JOB_OPENING_NOT_FOUND)
        );
        String url = jobOpening.getJobOpeningUrl();

        if (!url.startsWith("http")) {
            url = "https://" + url;
        }

        log.info("최종 리다이렉트 URL: {}", url);

        jobOpening.increaseViewCount();
        jobOpeningRepository.save(jobOpening);
        return url;
    }

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
