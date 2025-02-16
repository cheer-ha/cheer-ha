package com.project.cheerha.domain.notice.repository;

import com.project.cheerha.domain.notice.dto.JobOpeningDto;
import com.project.cheerha.domain.notice.dto.JobOpeningKeywordDto;
import com.project.cheerha.domain.notice.dto.UserDto;
import com.project.cheerha.domain.notice.dto.UserKeywordDto;
import java.util.List;

public interface NoticeCreationRepositoryQuery {

    // 모든 사용자의 식별자 및 이메일 조회
    List<UserDto> findAllUsers();

    // 모든 채용 공고의 식별자 및 URL 조회
    List<JobOpeningDto> findAllJobOpenings();

    // 중간 테이블(JobOpeningKeyword) 조회
    // 채용 공고의 식별자 및 키워드 식별자 목록 조회
    List<JobOpeningKeywordDto> findAllJobOpeningKeywords();

    // 중간 테이블(UserKeyword) 조회
    // 사용자의 식별자 및 키워드 식별자 목록 조회
    List<UserKeywordDto> findAllUserKeywords();
}