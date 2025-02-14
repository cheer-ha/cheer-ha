package com.project.cheerha.domain.keyword.service;

import com.project.cheerha.common.exception.CustomException;
import com.project.cheerha.common.exception.ErrorCode;
import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeywordFindByService {

    private final KeywordRepository keywordRepository;

    public Keyword findById(Long keywordId) {
        return keywordRepository.findById(keywordId)
            .orElseThrow(() -> new CustomException(ErrorCode.KEYWORD_NOT_FOUND));
    }
}
