package com.project.cheerha.domain.bookmark.repository;

import static com.project.cheerha.domain.bookmark.entity.QBookmark.bookmark;
import static com.project.cheerha.domain.jobopening.entity.QJobOpening.jobOpening;
import static com.project.cheerha.domain.user.entity.QUser.user;
import static com.querydsl.jpa.JPAExpressions.select;

import com.project.cheerha.domain.bookmark.dto.response.BookmarkCustomAgeResponseDto;
import com.project.cheerha.domain.bookmark.dto.response.QBookmarkCustomAgeResponseDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookmarkRepositoryQueryImpl implements BookmarkRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 사용자가 원하는 연령대의 사람들이 추가한 즐겨찾기를 상위 10개까지 보여주는 데 사용되는 로직입니다.
     * QueryDSL을 사용하여 bookmark에 있는 정보를 조회합니다.
     * bookmark에는 식별자, user식별자, jobopening 식별자만 존재합니다. (PK인 id와 FK만 2개 존재)
     * join을 사용하여 FK가 있는 user와 jobOpening의 정보도 조회할 수 있게 합니다.
     * user에서 연령대 정보를 가져오면서 between으로 최대연령과 최소연령을 사용자가 입력한 값으로 특정합니다.
     * 특정된 연령대에서 즐겨찾기 된 jobOpening의 정보들과 함께 서브쿼리를 사용하여 해당 즐겨찾기를 추가한 횟수를 count한 정보까지 보여줍니다.
     * 이 때 서브쿼리에서도 join과 where절을 사용하여 user 연령대, jobopening 정보까지 동일한 경우에만 count 되도록 설정합니다.
     * ExpressionUtils.as를 사용하여 별칭을 설정하여주고, 해당 별칭으로 전체 쿼리 조회 시 기준을 해당 count 값으로 내림차순 합니다.
     * limit을 10으로 걸어서 상위 10개만 보여주고 나머지를 안 보이게 설정합니다.
     * @return 상위 내용을 return 합니다.
     */
    @Override
    public List<BookmarkCustomAgeResponseDto> readTop10BookmarksByAgeBetween(int minAge, int maxAge) {

        return jpaQueryFactory.select(
            new QBookmarkCustomAgeResponseDto(
                jobOpening.id,
                jobOpening.title,
                jobOpening.company,
                jobOpening.educationLevel,
                jobOpening.employmentType,
                jobOpening.hiringStartAt,
                jobOpening.hiringEndAt,
                jobOpening.position,
                jobOpening.salary,
                jobOpening.minExperienceYears,
                jobOpening.maxExperienceYears,
                ExpressionUtils.as(
                    select(bookmark.id.count())
                        .from(bookmark)
                        .join(bookmark.user, user)
                        .join(bookmark.jobOpening)
                        .where(user.age.between(minAge, maxAge)
                            .and(bookmark.jobOpening.id.eq(jobOpening.id))),
                    "bookmarkCount"
                )
            ))
            .from(bookmark)
            .join(bookmark.jobOpening,jobOpening)
            .join(bookmark.user,user)
            .where(user.age.between(minAge,maxAge))
            .groupBy(
                jobOpening.id,
                jobOpening.title,
                jobOpening.company,
                jobOpening.educationLevel,
                jobOpening.employmentType,
                jobOpening.hiringStartAt,
                jobOpening.hiringEndAt,
                jobOpening.position,
                jobOpening.salary,
                jobOpening.minExperienceYears,
                jobOpening.maxExperienceYears
            )
            .orderBy(
                Expressions.numberPath(Long.class, "bookmarkCount").desc()
            )
            .limit(10)
            .fetch();
    }

}
