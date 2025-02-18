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

    public List<UserKeywordDto> findAllUserKeywords() {
        return repositoryQuery.findAllUserKeywords();
    }

    public List<JobOpeningKeywordDto> findAllJobOpeningKeywords(ZonedDateTime referenceTime) {
        return repositoryQuery.findAllJobOpeningKeywords(referenceTime);
    }
}