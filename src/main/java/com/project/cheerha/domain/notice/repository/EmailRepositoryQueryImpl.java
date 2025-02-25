package com.project.cheerha.domain.notice.repository;

import static com.project.cheerha.domain.jobopening.entity.QJobOpening.jobOpening;
import static com.project.cheerha.domain.keyword.entity.QJobOpeningKeyword.jobOpeningKeyword;
import static com.project.cheerha.domain.keyword.entity.QUserKeyword.userKeyword;
import static com.project.cheerha.domain.user.entity.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.project.cheerha.domain.notice.QUserDto;
import com.project.cheerha.domain.notice.UserDto;
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

    /**
     * 주어진 시간(referenceTime) 이후에 생성된 채용 공고들의 키워드와 URL 목록을 조회하는 메서드
     *
     * @param referenceTime 조회 시간
     * @return 채용 공고 키워드와 해당 키워드에 매칭되는 URL 목록을 매핑한 맵
     *         (Key: KeywordId, Value: 해당 키워드에 매칭되는 URL 목록)
     */
    @Override
    public Map<Long, List<String>> findAllJobOpeningKeywords(
        ZonedDateTime referenceTime
    ) {
        return queryFactory
            .from(jobOpeningKeyword)
            .join(jobOpeningKeyword.jobOpening, jobOpening)
            .where(jobOpening.createdAt.after(referenceTime)) // 기준 시간 이후 생성된 채용 공고
            .transform(
                groupBy(jobOpeningKeyword.keyword.id) // 키워드 ID별로 그룹화
                    .as(list(jobOpening.jobOpeningUrl)) // URL 목록을 그룹에 매핑
            );
    }

    /**
     * 모든 유저의 이메일과 해당 키워드를 조회하는 메서드
     *
     * @return 유저의 이메일과 그들의 관심 키워드를 포함한 UserDto 리스트
     */
    @Override
    public List<UserDto> findAllUserKeywords() {

        return queryFactory
            .select(
                new QUserDto(
                    userKeyword.keyword.id, // 사용자가 선택한 키워드 ID
                    user.email // 사용자 이메일
                )
            ).from(userKeyword)
            .join(userKeyword.user, user) // 사용자와 키워드 조인
            .fetch();
    }
}