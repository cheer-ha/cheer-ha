package com.project.cheerha.domain.data.repository;

import com.project.cheerha.domain.data.dto.response.ReadDataResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface DataRepositoryQuery {

    Page<ReadDataResponseDto> findAllByCondition(
            String education, LocalDateTime hiringStartPeriod,
            LocalDateTime hiringEndPeriod, String location,
            Integer career, String jobType,
            String requiredSkill, Pageable pageable);
}
