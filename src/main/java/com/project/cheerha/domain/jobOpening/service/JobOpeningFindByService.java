package com.project.cheerha.domain.jobopening.service;

import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobOpeningFindByService {

    private final JobOpeningRepository jobOpeningRepository;

    public JobOpening findById(Long jobOpeningId) {
        return jobOpeningRepository.findById(jobOpeningId)
            .orElseThrow(() -> new NotFoundException(DataErrorCode.JOB_OPENING_NOT_FOUND));
    }

    public JobOpening findByForUpdateViewCount(Long id) {
        return jobOpeningRepository.findByForUpdateViewCount(id)
            .orElseThrow(() -> new NotFoundException(DataErrorCode.JOB_OPENING_NOT_FOUND));
    }
}
