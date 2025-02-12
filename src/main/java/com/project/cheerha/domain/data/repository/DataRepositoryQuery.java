package com.project.cheerha.domain.data.repository;

import com.project.cheerha.domain.data.entity.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface DataRepositoryQuery {

    Page<Data> findAllByCondition(
            String education, LocalDate hiringStartPeriod,
            LocalDate hiringEndPeriod, String location,
            int career, String jobType,
            String requiredSkill, Pageable pageable);
}
