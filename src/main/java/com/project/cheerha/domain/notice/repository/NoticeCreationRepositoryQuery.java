package com.project.cheerha.domain.notice.repository;

import com.project.cheerha.domain.notice.dto.JobOpeningKeywordDto;
import com.project.cheerha.domain.notice.dto.UserKeywordDto;
import java.util.List;

public interface NoticeCreationRepositoryQuery {

    List<UserKeywordDto> findAllUserKeywords();

    List<JobOpeningKeywordDto> findAllJobOpeningKeywords();
}