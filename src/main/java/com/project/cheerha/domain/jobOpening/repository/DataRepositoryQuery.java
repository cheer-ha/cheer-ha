package com.project.cheerha.domain.jobOpening.repository;

import com.project.cheerha.domain.jobOpening.dto.request.ReadDataRequestDto;
import com.project.cheerha.domain.jobOpening.dto.response.ReadDataResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DataRepositoryQuery {

    Page<ReadDataResponseDto> findAllByCondition(
            ReadDataRequestDto requestDto, Pageable pageable);
}
