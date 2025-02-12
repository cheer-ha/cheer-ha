package com.project.cheerha.domain.data.repository;

import com.project.cheerha.domain.data.dto.request.ReadDataRequestDto;
import com.project.cheerha.domain.data.dto.response.ReadDataResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DataRepositoryQuery {

    Page<ReadDataResponseDto> findAllByCondition(
            ReadDataRequestDto requestDto, Pageable pageable);
}
