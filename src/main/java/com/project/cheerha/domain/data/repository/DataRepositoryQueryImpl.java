package com.project.cheerha.domain.data.repository;

import com.project.cheerha.domain.data.entity.Data;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.project.cheerha.domain.data.entity.QData.data;
import static com.project.cheerha.domain.keyword.entity.QDataKeyword.dataKeyword;
import static com.project.cheerha.domain.keyword.entity.QKeyword.keyword;

@Repository
@RequiredArgsConstructor
public class DataRepositoryQueryImpl implements DataRepositoryQuery{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Data> findAllByCondition(
        String education, LocalDateTime hiringStartPeriod,
        LocalDateTime hiringEndPeriod, String location,
        Integer career, String jobType,
        String requiredSkill, Pageable pageable
    ) {
        List<Data> dataList = queryFactory
            .selectFrom(data)
            .leftJoin(data.dataKeywords, dataKeyword)
            .leftJoin(dataKeyword.keyword, keyword)
            .where(
                containsKeywords(requiredSkill),
                eqEducation(education),
                geoHiringStartPeriod(hiringStartPeriod),
                leoHiringEndPeriod(hiringEndPeriod),
                eqLocation(location),
                leoCareer(career),
                eqJobType(jobType)
            )
            .distinct()
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        Long totalCount = Optional.ofNullable(
            queryFactory.select(Wildcard.count)
                .from(data)
                .leftJoin(data.dataKeywords, dataKeyword)
                .leftJoin(dataKeyword.keyword, keyword)
                .where(
                    containsKeywords(requiredSkill),
                    eqEducation(education),
                    geoHiringStartPeriod(hiringStartPeriod),
                    leoHiringEndPeriod(hiringEndPeriod),
                    eqLocation(location),
                    leoCareer(career),
                    eqJobType(jobType)
                ).fetchOne())
                .orElse(0L);

        return new PageImpl<>(dataList, pageable, totalCount);
    }

    // 입력된 자격 요건이 포함된 데이터만 가져오도록 하는 메서드
    // TODO : 모든 키워드가 포함된 데이터만 조회할지, 아니면 입력된 키워드 중 하나라도 포함되는 데이터를 조회할지 고민해봐야 합니다!
    private BooleanExpression containsKeywords(String requiredSkill) {
        return requiredSkill != null ? keyword.name.in(requiredSkill) : null;
    }

    // 입력된 학력과 같은 데이터만 가져오도록 하는 메서드
    private BooleanExpression eqEducation(String education) {
        return education != null ? data.education.eq(education) : null;
    }

    // 입력된 시작 날짜보다 큰 데이터만 가져오도록 하는 메서드
    private BooleanExpression geoHiringStartPeriod(LocalDateTime hiringStartPeriod) {
        return hiringStartPeriod != null ? data.hiringStartPeriod.loe(hiringStartPeriod) : null;
    }

    // 입력된 마감 날짜보다 작은 데이터만 가져오도록 하는 메서드
    private BooleanExpression leoHiringEndPeriod(LocalDateTime hiringEndPeriod) {
        return hiringEndPeriod != null ? data.hiringEndPeriod.loe(hiringEndPeriod) : null;
    }

    // 동일한 근무 지역의 데이터만 가져오도록 하는 메서드
    private BooleanExpression eqLocation(String location) {
        return location != null ? data.location.eq(location) : null;
    }

    // 입력된 경력 이하의 데이터만 가져오도록 하는 메서드
    private BooleanExpression leoCareer(Integer career) {
        return career != null ? data.career.loe(career) : null;
    }

    // 입력된 근무 형태와 동일한 데이터만 가져오도록 하는 메서드
    private BooleanExpression eqJobType(String jobType) {
        return jobType != null ? data.jobType.eq(jobType) : null;
    }
}
