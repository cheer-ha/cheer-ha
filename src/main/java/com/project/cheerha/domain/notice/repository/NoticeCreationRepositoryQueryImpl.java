package com.project.cheerha.domain.notice.repository;

import com.project.cheerha.domain.jobOpening.entity.QJobOpening;
import com.project.cheerha.domain.keyword.entity.QJobOpeningKeyword;
import com.project.cheerha.domain.keyword.entity.QUserKeyword;
import com.project.cheerha.domain.notice.dto.JobOpeningDto;
import com.project.cheerha.domain.notice.dto.JobOpeningKeywordDto;
import com.project.cheerha.domain.notice.dto.UserDto;
import com.project.cheerha.domain.notice.dto.UserKeywordDto;
import com.project.cheerha.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeCreationRepositoryQueryImpl implements NoticeCreationRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    // 모든 채용 공고의 식별자 및 URL을 조회하는 메서드
    @Override
    public List<JobOpeningDto> findAllJobOpenings() {
        QJobOpening jobOpening = QJobOpening.jobOpening;

        return queryFactory
            .select(Projections.constructor(
                    JobOpeningDto.class,
                    jobOpening.id,
                    jobOpening.jobOpeningUrl
                )
            ).from(jobOpening)
            .fetch();
    }

    // 모든 사용자의 식별자 및 이메일 조회
    @Override
    public List<UserDto> findAllUsers() {
        QUser user = QUser.user;

        return queryFactory
            .select(Projections.constructor(
                    UserDto.class,
                    user.id,
                    user.email
                )
            ).from(user)
            .fetch();
    }

    // 중간 테이블(JobOpeningKeyword) 조회
    // 채용 공고의 식별자 및 키워드 식별자 목록 조회
    public List<JobOpeningKeywordDto> findAllJobOpeningKeywords() {
        QJobOpeningKeyword jobOpeningKeyword = QJobOpeningKeyword.jobOpeningKeyword;

        return queryFactory
            .select(Projections.constructor(
                    JobOpeningKeywordDto.class,
                    jobOpeningKeyword.jobOpening.id,
                    jobOpeningKeyword.keyword.id
                )
            ).from(jobOpeningKeyword)
            .fetch();
    }

    // 중간 테이블(UserKeyword) 조회
    // 사용자의 식별자 및 키워드 식별자 목록 조회
    @Override
    public List<UserKeywordDto> findAllUserKeywords() {
        QUserKeyword userKeyword = QUserKeyword.userKeyword;

        return queryFactory
            .select(Projections.constructor(
                    UserKeywordDto.class,
                    userKeyword.user.id,
                    userKeyword.keyword.id
                )
            ).from(userKeyword)
            .fetch();
    }
}