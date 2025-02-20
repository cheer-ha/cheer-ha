package com.project.cheerha.domain.keyword.service;

import com.project.cheerha.domain.keyword.dto.request.ReadKeywordAgeRequestDto;
import com.project.cheerha.domain.keyword.dto.response.KeywordCustomAgeResponseDto;
import com.project.cheerha.domain.keyword.dto.response.KeywordDto;
import com.project.cheerha.domain.keyword.dto.response.ReadKeywordResponseDto;
import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.keyword.repository.KeywordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional(readOnly = true)
    public ReadKeywordResponseDto readKeywords(String searchTerm) {
        List<Keyword> keywordList;

        if (searchTerm == null || searchTerm.isEmpty()) {
            keywordList = keywordRepository.findAll();
        } else {
            keywordList = keywordRepository.findByNameContaining(searchTerm);
        }

        List<KeywordDto> keywordDtoList = keywordList.stream()
            .map(keyword -> KeywordDto.toKeywordDto(
                    keyword.getId(),
                    keyword.getName()
                )
            )
            .toList();

        return ReadKeywordResponseDto.toDto(keywordDtoList);
    }

    public List<KeywordCustomAgeResponseDto> readTop10KeywordsByAgeGroup (ReadKeywordAgeRequestDto requestDto) {
        int minAge = requestDto.minAge();
        int maxAge = requestDto.maxAge();
        return keywordRepository.readTop10KeywordsByAgeGroup(minAge, maxAge);
    }
}