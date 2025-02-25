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

    /**
     * 채용공고를 조회하는 메서드입니다.
     * 해당 채용공고가 없으면 페이지가 존재하지 않는다는 예외처리가 됩니다.
     * @param jobOpeningId
     * @return 특정 id를 사용하는 채용공고 정보를 리턴
     */
    public JobOpening findById(Long jobOpeningId) {
        return jobOpeningRepository.findById(jobOpeningId)
            .orElseThrow(() -> new NotFoundException(DataErrorCode.JOB_OPENING_NOT_FOUND));
    }
}
