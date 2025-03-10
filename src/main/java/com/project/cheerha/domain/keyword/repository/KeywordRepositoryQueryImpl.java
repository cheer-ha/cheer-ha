package com.project.cheerha.domain.keyword.repository;

import com.project.cheerha.domain.keyword.dto.response.KeywordCustomAgeResponseDto;
import com.project.cheerha.domain.keyword.dto.response.QKeywordCustomAgeResponseDto;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.cheerha.domain.keyword.entity.QKeyword.keyword;
import static com.project.cheerha.domain.user.entity.QUser.user;
import static com.project.cheerha.domain.userkeyword.entity.QUserKeyword.userKeyword;

@Repository
@RequiredArgsConstructor
public class KeywordRepositoryQueryImpl implements KeywordRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 연령대 별 인기 키워드 상위 10개를 조회하는 메서드입니다.
     *
     * QueryDSL을 사용하여 keyword 엔티티에 대한 정보를 조회하며, keyword와 user 엔티티를 join합니다.
     * 사용자가 입력한 연령대(minAge, maxAge) 조건에 맞는 user 정보를 기준으로 키워드(keyword)를 필터링합니다.
     * 연령대 필터링은 `user.age.between(minAge, maxAge)`로 처리됩니다.
     * `Expressions.numberTemplate`을 사용한 키워드 수(count)를 기준으로 내림차순으로 정렬합니다.
     * 결과적으로, 조건에 맞는 상위 10개의 keyword를 조회하며, `limit(10)`으로 상위 10개만 결과로 반환됩니다.
     *
     * @param minAge 사용자가 설정한 최소 연령
     * @param maxAge 사용자가 설정한 최대 연령
     * @return 사용자가 입력한 연령대에서 상위 10개의 인기 keyword 정보
     */
    @Override
    public List<KeywordCustomAgeResponseDto> readTop10KeywordsByAgeGroup(int minAge, int maxAge) {
        return jpaQueryFactory.select(
            new QKeywordCustomAgeResponseDto(
                keyword.id,
                keyword.name,
                Expressions.numberTemplate(Long.class, "COUNT({0})", userKeyword.id).as("keywordCount")
            ))
            .from(userKeyword)
            .join(userKeyword.keyword, keyword)
            .join(userKeyword.user, user)
            .where(user.age.between(minAge, maxAge))
            .groupBy(keyword.id, keyword.name)
            .orderBy(Expressions.numberPath(Long.class, "keywordCount").desc())
            .limit(10)
            .fetch();
    }
}
