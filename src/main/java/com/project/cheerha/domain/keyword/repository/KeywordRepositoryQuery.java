package com.project.cheerha.domain.keyword.repository;

import com.project.cheerha.domain.keyword.dto.response.ReadKeywordResponseDto;
import java.util.List;

public interface KeywordRepositoryQuery {

   List<ReadKeywordResponseDto> readTop10KeywordsByAgeGroup(int minAge, int maxAge);

}
