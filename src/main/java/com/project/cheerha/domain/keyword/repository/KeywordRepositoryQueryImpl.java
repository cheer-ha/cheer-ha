package com.project.cheerha.domain.keyword.repository;

import static com.project.cheerha.domain.keyword.entity.QKeyword.keyword;
import static com.project.cheerha.domain.keyword.entity.QUserKeyword.userKeyword;
import static com.project.cheerha.domain.user.entity.QUser.user;
import static com.querydsl.jpa.JPAExpressions.select;

import com.project.cheerha.domain.keyword.dto.response.KeywordCustomAgeResponseDto;
import com.project.cheerha.domain.keyword.dto.response.QKeywordCustomAgeResponseDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KeywordRepositoryQueryImpl implements KeywordRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<KeywordCustomAgeResponseDto> readTop10KeywordsByAgeGroup(int minAge, int maxAge) {

       return jpaQueryFactory.select(
                new QKeywordCustomAgeResponseDto(
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
