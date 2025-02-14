package com.project.cheerha.domain.jobOpening.service;

import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
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
            .orElseThrow(() -> new NotFoundException(DataErrorCode.JOB_OPENING_NOT_FOUND));
    }
}
