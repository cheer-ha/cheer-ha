package com.project.cheerha.domain.bookmark.repository;

import static com.project.cheerha.domain.bookmark.entity.QBookmark.bookmark;
import static com.project.cheerha.domain.jobOpening.entity.QJobOpening.jobOpening;
import static com.project.cheerha.domain.user.entity.QUser.user;
import static com.querydsl.jpa.JPAExpressions.select;

import com.project.cheerha.domain.bookmark.dto.request.ReadBookmarkAgeRequestDto;
import com.project.cheerha.domain.bookmark.dto.response.BookmarkCustomAgeResponseDto;
import com.project.cheerha.domain.bookmark.dto.response.QBookmarkCustomAgeResponseDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class BookmarkRepositoryQueryImpl implements BookmarkRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BookmarkCustomAgeResponseDto> readTop10BookmarksByAgeGroup(
        ReadBookmarkAgeRequestDto requestDto, Pageable pageable) {

        List<BookmarkCustomAgeResponseDto> bookmarksByCustomAgeList = jpaQueryFactory
            .select(new QBookmarkCustomAgeResponseDto(
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
                        .where(user.age.between(requestDto.minAge(), requestDto.maxAge())
                            .and(bookmark.jobOpening.id.eq(jobOpening.id))),
                    "bookmarkCount"
                )
            ))
            .from(bookmark)
            .join(bookmark.jobOpening,jobOpening)
            .join(bookmark.user,user)
            .where(user.age.between(requestDto.minAge(),requestDto.maxAge()))
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
            .orderBy(Expressions.numberPath(Long.class, "bookmarkCount").desc())
            .offset(pageable.getOffset())
            .limit(10)
            .fetch();

        Long totalCount = Optional.ofNullable(jpaQueryFactory
            .select(Wildcard.count)
            .from(bookmark)
            .join(bookmark.user, user) //연령대 조회를 위해 이 쪽에서도 조인을 걸어야 함.
            .where(user.age.between(requestDto.minAge(), requestDto.maxAge()))
            .fetchOne()).orElse(0L);

        return new PageImpl<>(bookmarksByCustomAgeList, pageable, totalCount);
    }

}
