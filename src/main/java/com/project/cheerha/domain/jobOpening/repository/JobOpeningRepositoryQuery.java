package com.project.cheerha.domain.jobOpening.repository;

import com.project.cheerha.domain.jobOpening.dto.request.ReadJobOpeningRequestDto;
import com.project.cheerha.domain.jobOpening.dto.response.ReadJobOpeningResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobOpeningRepositoryQuery {

    Page<ReadJobOpeningResponseDto> findAllByCondition(
            ReadJobOpeningRequestDto requestDto, Pageable pageable);
}
