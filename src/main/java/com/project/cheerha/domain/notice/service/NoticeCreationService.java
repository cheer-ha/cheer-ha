package com.project.cheerha.domain.notice.service;

import com.project.cheerha.domain.notice.dto.JobOpeningKeywordDto;
import com.project.cheerha.domain.notice.dto.UserKeywordDto;
import com.project.cheerha.domain.notice.repository.NoticeCreationRepositoryQuery;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeCreationService {

    private final NoticeCreationRepositoryQuery repositoryQuery;

    // 데이터베이스에서 사용자 관련 정보를 조회해오는 메서드
    public List<UserKeywordDto> findAllUserKeywords() {
        return repositoryQuery.findAllUserKeywords();
    }

    // 데이터베이스에서 채용 공고 관련 정보를 조회해오는 메서드
    public List<JobOpeningKeywordDto> findAllJobOpeningKeywords(ZonedDateTime referenceTime) {
        return repositoryQuery.findAllJobOpeningKeywords(referenceTime);
    }
}