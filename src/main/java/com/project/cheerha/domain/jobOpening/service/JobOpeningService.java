package com.project.cheerha.domain.jobOpening.service;

import com.project.cheerha.common.exception.CustomException;
import com.project.cheerha.common.exception.ErrorCode;
import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import com.project.cheerha.domain.jobOpening.repository.JobOpeningRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobOpeningService {

    private final JobOpeningRepository jobOpeningRepository;

    public String getJobOpeningUrlAndIncreaseViewCount(Long id) {
        JobOpening jobOpening = jobOpeningRepository.findById(id).orElseThrow(
            () -> new CustomException(ErrorCode.URL_NOT_FOUND)
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
}
