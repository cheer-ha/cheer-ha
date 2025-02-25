package com.project.cheerha.domain.notice.repository;

import com.project.cheerha.domain.notice.dto.UserDto;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface EmailRepositoryQuery {

    /**
     * 조회 기준으로 모든 채용 공고 정보를 조회하는 메서드
     *
     * @param referenceTime 조회 시간
     * @return 채용 공고 키워드와 해당 키워드가 연관된 URL 목록을 매핑한 맵
     * Key: KeywordId (키워드 식별자)
     * Value: 해당 키워드로 매칭되는 URL 목록
     */
    Map<Long, List<String>> findAllJobOpeningKeywords(ZonedDateTime referenceTime);

    /**
     * 모든 사용자의 이메일 및 등록한 키워드를 조회하는 메서드
     * @return 사용자 이메일과 선택한 키워드를 포함한 UserDto 리스트
     */
    List<UserDto> findAllUserKeywords();
}