package com.project.cheerha.domain.data.controller;

import com.project.cheerha.domain.data.dto.response.ReadDataResponseDto;
import com.project.cheerha.domain.data.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping("/search")
@RestController
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    @GetMapping
    public ResponseEntity<Page<ReadDataResponseDto>> readData(
        @RequestParam(required = false) String education,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime hiringStartPeriod,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime hiringEndPeriod,
        @RequestParam(required = false) String location,
        @RequestParam(required = false) Integer career,
        @RequestParam(required = false) String jobType,
        @RequestParam(required = false) String requiredSkill,
        Pageable pageable
    ) {
        Page<ReadDataResponseDto> dtoPage = dataService.readData(
            education,
            hiringStartPeriod,
            hiringEndPeriod,
            location,
            career,
            jobType,
            requiredSkill,
            pageable);

        return ResponseEntity.status(HttpStatus.OK).body(dtoPage);
    }
}
