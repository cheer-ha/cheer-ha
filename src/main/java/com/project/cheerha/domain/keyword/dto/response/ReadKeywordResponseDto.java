package com.project.cheerha.domain.keyword.dto.response;

import java.util.List;

public record ReadKeywordResponseDto(List<String> allKeywordNameList) {

    public static ReadKeywordResponseDto toDto(List<String> allKeywordNameList) {
        return new ReadKeywordResponseDto(allKeywordNameList);
    }
}