package com.project.cheerha.domain.data.service;

import com.project.cheerha.domain.data.dto.response.ReadDataResponseDto;
import com.project.cheerha.domain.data.entity.Data;
import com.project.cheerha.domain.data.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DataService {

    private final DataRepository dataRepository;

    @Transactional(readOnly = true)
    public Page<ReadDataResponseDto> readData(
            String education, LocalDate hiringStartPeriod,
            LocalDate hiringEndPeriod,
            String location, int career,
            String jobType, String requiredSkill,
            Pageable pageable
    ) {
        Page<Data> dataPage = dataRepository.findAllByCondition(
                education, hiringStartPeriod,
                hiringEndPeriod, location, career,
                jobType, requiredSkill,
                pageable);

        Page<ReadDataResponseDto> dtoPage = dataPage.map(ReadDataResponseDto::toDto);

        return dtoPage;
    }
}
