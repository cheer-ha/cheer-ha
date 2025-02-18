package com.project.cheerha.domain.keyword.repository;

import static com.project.cheerha.domain.keyword.entity.QKeyword.keyword;
import static com.project.cheerha.domain.keyword.entity.QUserKeyword.userKeyword;
import static com.project.cheerha.domain.user.entity.QUser.user;
import static com.querydsl.jpa.JPAExpressions.select;

import com.project.cheerha.domain.keyword.dto.response.KeywordCustomAgeResponseDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KeywordRepositoryQueryImpl implements KeywordRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<KeywordCustomAgeResponseDto> readTop10KeywordsByAgeGroup(int minAge, int maxAge) {

        /**
         * ExpressionUtils 이 부분 별칭 구현해서 그 별칭 기준으로 정렬을 위해 추가한 구문입니다.
         * 인기 순으로 조회하려고 보니 join도 많이 들어가고 DB 안에 있는 값을 직접 가져와서 정렬을 한 탓인지 느립니다.
         * 일단 기본 구현이란 생각으로 했고요, 이 부분 성능 개선 필수입니다. age 값이 없어서 대신 경력으로 돌려봤는데 체감상 느려요. 엄청!
         */

        return jpaQueryFactory.select(
                Projections.constructor(
                    KeywordCustomAgeResponseDto.class,
                    keyword.id,
                    keyword.name,
                    ExpressionUtils.as(
                        select(userKeyword.id.count())
                            .from(userKeyword)
                            .join(userKeyword.user, user)
                            .where(user.age.between(minAge, maxAge)
                                    .and(userKeyword.keyword.id.eq(keyword.id))),
                        "keywordCount")
                ))
            .from(userKeyword)
            .join(userKeyword.keyword, keyword)
            .join(userKeyword.user, user)
            .where(user.age.between(minAge, maxAge))
            .groupBy(keyword.id, keyword.name)
            .orderBy(
                Expressions.numberPath(Long.class, "keywordCount").desc()
            )
            .limit(10)
            .fetch();
    }
}
