package com.project.cheerha.domain.keyword.repository;


import com.project.cheerha.domain.keyword.dto.response.ReadKeywordResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KeywordRepositoryQueryImpl implements KeywordRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ReadKeywordResponseDto> readTop10KeywordsByAgeGroup(int minAge,int maxAge) {
//        List<ReadKeywordResponseDto> Top10KeywordsByAge = jpaQueryFactory.select(
//            Projections.constructor(
//                ReadKeywordResponseDto.class,
//                keyword.name,
//                select(Wildcard.count).from(keyword).where(keyword.name.eq(keyword.name))
//            )
//        ).from(keyword)
//            .where(
//                user.
//            )

        return List.of();
    }
}
