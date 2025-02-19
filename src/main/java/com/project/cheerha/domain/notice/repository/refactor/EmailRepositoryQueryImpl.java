package com.project.cheerha.domain.notice.repository.refactor;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.project.cheerha.domain.jobOpening.entity.QJobOpening;
import com.project.cheerha.domain.keyword.entity.QJobOpeningKeyword;
import com.project.cheerha.domain.keyword.entity.QUserKeyword;
import com.project.cheerha.domain.notice.dto.refactor.QUserKeywordDto;
import com.project.cheerha.domain.notice.dto.refactor.UserKeywordDto;
import com.project.cheerha.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailRepositoryQueryImpl implements EmailRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserKeywordDto> findAllUserKeywords() {
        QUserKeyword userKeyword = QUserKeyword.userKeyword;
        QUser user = QUser.user;

        return queryFactory
            .select(
                new QUserKeywordDto(
                    userKeyword.keyword.id,
                    user.email
                )
            ).from(userKeyword)
            .join(userKeyword.user, user)
            .fetch();
    }

    @Override
    public Map<Long, List<String>> findAllJobOpeningKeywords(ZonedDateTime referenceTime) {
        QJobOpeningKeyword jobOpeningKeyword = QJobOpeningKeyword.jobOpeningKeyword;
        QJobOpening jobOpening = QJobOpening.jobOpening;

        return queryFactory
            .from(jobOpeningKeyword)
            .join(jobOpeningKeyword.jobOpening, jobOpening)
            .where(jobOpening.createdAt.after(referenceTime))
            .transform(
                groupBy(
                    jobOpeningKeyword.keyword.id)
                    .as(list(jobOpening.jobOpeningUrl)
                    )
            );
    }
}