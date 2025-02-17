package com.project.cheerha.domain.notice.repository;

import com.project.cheerha.domain.jobOpening.entity.QJobOpening;
import com.project.cheerha.domain.keyword.entity.QJobOpeningKeyword;
import com.project.cheerha.domain.keyword.entity.QUserKeyword;
import com.project.cheerha.domain.notice.dto.JobOpeningKeywordDto;
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

    @Override
    public List<UserKeywordDto> findAllUserKeywords() {
        QUserKeyword userKeyword = QUserKeyword.userKeyword;
        QUser user = QUser.user;

        return queryFactory
            .select(Projections.constructor(
                    UserKeywordDto.class,
                    userKeyword.user.id,
                    userKeyword.keyword.id,
                    user.email
                )
            )
            .from(userKeyword)
            .join(userKeyword.user, user)
            .fetch();
    }

    public List<JobOpeningKeywordDto> findAllJobOpeningKeywords() {
        QJobOpeningKeyword jobOpeningKeyword = QJobOpeningKeyword.jobOpeningKeyword;
        QJobOpening jobOpening = QJobOpening.jobOpening;

        return queryFactory
            .select(Projections.constructor(
                    JobOpeningKeywordDto.class,
                    jobOpeningKeyword.jobOpening.id,
                    jobOpeningKeyword.keyword.id,
                    jobOpening.jobOpeningUrl
                )
            )
            .from(jobOpeningKeyword)
            .join(jobOpeningKeyword.jobOpening, jobOpening)
            .fetch();
    }
}