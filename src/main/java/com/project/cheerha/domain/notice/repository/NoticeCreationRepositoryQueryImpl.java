package com.project.cheerha.domain.notice.repository;

import com.project.cheerha.domain.jobOpening.entity.QJobOpening;
import com.project.cheerha.domain.keyword.entity.QJobOpeningKeyword;
import com.project.cheerha.domain.keyword.entity.QUserKeyword;
import com.project.cheerha.domain.notice.dto.JobOpeningKeywordDto;
import com.project.cheerha.domain.notice.dto.QJobOpeningKeywordDto;
import com.project.cheerha.domain.notice.dto.QUserKeywordDto;
import com.project.cheerha.domain.notice.dto.UserKeywordDto;
import com.project.cheerha.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeCreationRepositoryQueryImpl implements NoticeCreationRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    // 데이터베이스에서 사용자 관련 정보를 조회해오는 메서드
    @Override
    public List<UserKeywordDto> findAllUserKeywords() {
        QUserKeyword userKeyword = QUserKeyword.userKeyword;
        QUser user = QUser.user;

        return queryFactory
            .select(
                new QUserKeywordDto(
                    userKeyword.user.id,
                    userKeyword.keyword.id,
                    user.email
                )
            ).from(userKeyword)
            .join(userKeyword.user, user)
            .fetch();
    }

    // 데이터베이스에서 채용 공고 관련 정보를 조회해오는 메서드
    public List<JobOpeningKeywordDto> findAllJobOpeningKeywords(ZonedDateTime referenceTime) {
        QJobOpeningKeyword jobOpeningKeyword = QJobOpeningKeyword.jobOpeningKeyword;
        QJobOpening jobOpening = QJobOpening.jobOpening;

        return queryFactory
            .select(new QJobOpeningKeywordDto(
                    jobOpeningKeyword.jobOpening.id,
                    jobOpeningKeyword.keyword.id,
                    jobOpening.jobOpeningUrl
                )
            )
            .from(jobOpeningKeyword)
            .join(jobOpeningKeyword.jobOpening, jobOpening)
            .where(jobOpening.createdAt.after(referenceTime))
            .fetch();
    }
}