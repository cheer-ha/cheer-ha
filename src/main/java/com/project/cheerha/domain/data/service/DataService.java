package com.project.cheerha.domain.data.service;

import com.project.cheerha.domain.data.dto.request.ReadDataRequestDto;
import com.project.cheerha.domain.data.dto.response.ReadDataResponseDto;
import com.project.cheerha.domain.data.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DataService {

    private final DataRepository dataRepository;

    @Transactional
    public Page<ReadDataResponseDto> readData(
            ReadDataRequestDto requestDto,
            Pageable pageable
    ) {
        Page<ReadDataResponseDto> dtoPage = dataRepository.findAllByCondition(
                requestDto, pageable);

        return dtoPage;
    }
}
