package com.project.cheerha.domain.keyword.dto.response;

public record KeywordDto(Long id, String name) {

    public static KeywordDto toKeywordDto(Long id, String name) {
        return new KeywordDto(id, name);
    }
}
