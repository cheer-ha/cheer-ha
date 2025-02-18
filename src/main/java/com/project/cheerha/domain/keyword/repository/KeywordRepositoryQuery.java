package com.project.cheerha.domain.keyword.repository;

import com.project.cheerha.domain.keyword.dto.response.KeywordCustomAgeResponseDto;
import java.util.List;

public interface KeywordRepositoryQuery {

   List<KeywordCustomAgeResponseDto> readTop10KeywordsByAgeGroup(int minAge, int maxAge);

}
