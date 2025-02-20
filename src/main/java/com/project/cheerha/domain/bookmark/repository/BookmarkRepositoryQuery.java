package com.project.cheerha.domain.bookmark.repository;

import com.project.cheerha.domain.bookmark.dto.response.BookmarkCustomAgeResponseDto;
import java.util.List;

public interface BookmarkRepositoryQuery {

    /**
     * List로 특정 연령대 사람들이 추가한 즐겨찾기 상위 10개를 보여주는 로직을 작성하기 위한 메서드입니다.
     * @param minAge 최소나이
     * @param maxAge 최대나이
     * @return 즐겨찾기 상위 10개 리스트
     */
    List<BookmarkCustomAgeResponseDto> readTop10BookmarksByAgeGroup(int minAge, int maxAge);
}
