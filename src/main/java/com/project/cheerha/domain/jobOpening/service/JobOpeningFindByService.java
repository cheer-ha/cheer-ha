package com.project.cheerha.domain.jobOpening.service;

import com.project.cheerha.common.exception.CustomException;
import com.project.cheerha.common.exception.ErrorCode;
import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import com.project.cheerha.domain.jobOpening.repository.JobOpeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobOpeningFindByService {

    private final JobOpeningRepository jobOpeningRepository;

    public JobOpening findById(Long jobOpeningId) {
        return jobOpeningRepository.findById(jobOpeningId)
            .orElseThrow(() -> new CustomException(ErrorCode.JOB_OPENING_NOT_FOUND));
    }
}
