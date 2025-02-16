package com.project.cheerha.domain.notice.service;

import com.project.cheerha.domain.notice.dto.JobOpeningDto;
import com.project.cheerha.domain.notice.dto.JobOpeningKeywordDto;
import com.project.cheerha.domain.notice.dto.UserDto;
import com.project.cheerha.domain.notice.dto.UserKeywordDto;
import com.project.cheerha.domain.notice.repository.NoticeCreationRepositoryQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeCreationService {

    private final NoticeCreationRepositoryQuery repositoryQuery;

    public List<UserDto> findAllUsers() {
        return repositoryQuery.findAllUsers();
    }

    public List<JobOpeningDto> findAllJobOpenings() {
        return repositoryQuery.findAllJobOpenings();
    }

    public List<JobOpeningKeywordDto> findAllJobOpeningKeywords() {
        return repositoryQuery.findAllJobOpeningKeywords();
    }

    public List<UserKeywordDto> findAllUserKeywords() {
        return repositoryQuery.findAllUserKeywords();
    }
}
