package com.project.cheerha.domain.jobopening.repository;

import com.project.cheerha.domain.jobopening.dto.request.ReadJobOpeningRequestDto;
import com.project.cheerha.domain.jobopening.dto.response.ReadJobOpeningResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobOpeningRepositoryQuery {

    Page<ReadJobOpeningResponseDto> findAllByCondition(
            ReadJobOpeningRequestDto requestDto, Pageable pageable);

    // 인기 채용공고 조회 - 조회수 기준 상위 100개
    Page<ReadJobOpeningResponseDto> findTop100PopularJobOpenings(Pageable pageable);
}
