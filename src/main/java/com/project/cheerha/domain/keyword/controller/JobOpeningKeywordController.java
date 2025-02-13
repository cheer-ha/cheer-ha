package com.project.cheerha.domain.keyword.controller;

import com.project.cheerha.common.exception.CustomException;
import com.project.cheerha.common.exception.ErrorCode;
import com.project.cheerha.domain.data.dto.request.ReadDataRequestDto;
import com.project.cheerha.domain.data.dto.response.ReadDataResponseDto;
import com.project.cheerha.domain.data.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/search")
@RestController
@RequiredArgsConstructor
public class JobOpeningKeywordController {

    private final DataService dataService;

    @GetMapping
    public ResponseEntity<Page<ReadDataResponseDto>> readData(
            @ModelAttribute ReadDataRequestDto requestDto,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = validatePageSize(page, size);

        Page<ReadDataResponseDto> dtoPage = dataService.readData(
            requestDto, pageable
            );

        return ResponseEntity.status(HttpStatus.OK).body(dtoPage);
    }

    private Pageable validatePageSize(int page, int size) {
        if (page < 1 || size < 1) {
            throw new CustomException(ErrorCode.PAGING_ERROR);
        }

        return PageRequest.of(page - 1, size);
    }
}
