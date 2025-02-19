package com.project.cheerha.domain.notice.repository.draft;

import com.project.cheerha.domain.notice.dto.draft.JobOpeningKeywordDto;
import com.project.cheerha.domain.notice.dto.draft.UserKeywordDto;
import java.time.ZonedDateTime;
import java.util.List;

public interface EmailRepositoryQuery {

    // 데이터베이스에서 사용자 관련 정보를 조회해오는 메서드
    List<UserKeywordDto> findAllUserKeywords();

    // 데이터베이스에서 채용 공고 관련 정보를 조회해오는 메서드
    List<JobOpeningKeywordDto> findAllJobOpeningKeywords(ZonedDateTime referenceTime);
}