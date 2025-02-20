package com.project.cheerha.domain.keyword.repository;

import com.project.cheerha.domain.keyword.dto.response.KeywordCustomAgeResponseDto;
import java.util.List;

public interface KeywordRepositoryQuery {

   /**
    * List로 특정 연령대 사람들이 추가한 키워드 상위 10개를 보여주는 로직을 작성하기 위한 메서드입니다.
    * @param minAge 최소나이
    * @param maxAge 최대나이
    * @return 키워드 10개 리스트
    */
   List<KeywordCustomAgeResponseDto> readTop10KeywordsByAgeGroup(int minAge, int maxAge);

}
