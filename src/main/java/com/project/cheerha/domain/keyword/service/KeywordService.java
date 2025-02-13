package com.project.cheerha.domain.keyword.service;

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
    public ReadKeywordResponseDto readAllKeywords() {
        List<Keyword> allKeywordList = keywordRepository.findAll();

        List<String> keywordNameList = Keyword.extractNameFromEntity(allKeywordList);

        return ReadKeywordResponseDto.toDto(keywordNameList);
    }
}