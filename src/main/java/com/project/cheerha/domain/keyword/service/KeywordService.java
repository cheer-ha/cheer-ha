package com.project.cheerha.domain.keyword.service;

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
    public ReadKeywordResponseDto readKeywords(String search) {
        List<Keyword> keywordList;

        if (search == null || search.isEmpty()) {
            keywordList = keywordRepository.findAll();
        } else {
            keywordList = keywordRepository.findByNameContaining(search);
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
}