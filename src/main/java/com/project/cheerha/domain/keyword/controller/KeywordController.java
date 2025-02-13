package com.project.cheerha.domain.keyword.controller;

import com.project.cheerha.domain.keyword.dto.response.ReadKeywordResponseDto;
import com.project.cheerha.domain.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/keywords")
@RestController
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping
    public ResponseEntity<ReadKeywordResponseDto> readAllKeywords(
        @RequestParam(value = "search", required = false) String search
    ) {
        ReadKeywordResponseDto responseDto = keywordService.readKeywords(search);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
