package com.project.cheerha.domain.bookmark.repository;

import static com.project.cheerha.domain.bookmark.entity.QBookmark.bookmark;
import static com.project.cheerha.domain.jobopening.entity.QJobOpening.jobOpening;
import static com.project.cheerha.domain.user.entity.QUser.user;

import com.project.cheerha.domain.bookmark.dto.response.BookmarkCustomAgeResponseDto;
import com.project.cheerha.domain.bookmark.dto.response.QBookmarkCustomAgeResponseDto;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryQueryImpl implements BookmarkRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 사용자가 입력한 연령대에 해당하는 사람들이 즐겨찾기한 상위 10개의 jobOpening 정보를 조회하는 로직입니다.
     * QueryDSL을 사용하여 bookmark 엔티티에 대한 정보를 조회하며, jobOpening과 user 엔티티를 조인합니다.
     * bookmark 테이블은 식별자(id), user 식별자(userId), jobOpening 식별자(jobOpeningId)로 구성되어 있으며,
     * 각각의 외래 키를 통해 user와 jobOpening 테이블을 조인하여 관련 정보를 가져옵니다.
     * 사용자가 입력한 연령대(minAge, maxAge) 조건에 맞는 user 정보를 기준으로 즐겨찾기(bookmark)를 필터링합니다.
     * 연령대 필터링은 `user.age.between(minAge, maxAge)`로 처리됩니다.
     * `Expressions.numberTemplate`을 사용한 즐겨찾기 수(count)를 기준으로 내림차순으로 정렬합니다.
     * 결과적으로, 조건에 맞는 상위 10개의 즐겨찾기된 jobOpening을 조회하며, `limit(10)`으로 상위 10개만 결과로 반환됩니다.
     * @param minAge 사용자가 설정한 최소 연령
     * @param maxAge 사용자가 설정한 최대 연령
     * @return 사용자가 입력한 연령대에서 즐겨찾기된 상위 10개의 jobOpening 정보
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
                                Expressions.numberTemplate(Long.class, "COUNT({0})", bookmark.id).as("bookmarkCount")
                        )
                )
                .from(bookmark)
                .join(bookmark.jobOpening, jobOpening)
                .join(bookmark.user, user)
                .where(user.age.between(minAge, maxAge))
                .groupBy(jobOpening.id)
                .orderBy(Expressions.numberPath(Long.class, "bookmarkCount").desc())
                .limit(10)
                .fetch();
    }
}
