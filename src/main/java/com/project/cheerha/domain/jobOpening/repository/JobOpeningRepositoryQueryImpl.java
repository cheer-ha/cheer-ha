package com.project.cheerha.domain.jobOpening.repository;

import com.project.cheerha.domain.jobOpening.dto.request.ReadJobOpeningRequestDto;
import com.project.cheerha.domain.jobOpening.dto.response.ReadJobOpeningResponseDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.cheerha.domain.data.entity.QData.data;
import static com.project.cheerha.domain.keyword.entity.QDataKeyword.dataKeyword;
import static com.project.cheerha.domain.keyword.entity.QKeyword.keyword;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JobOpeningRepositoryQueryImpl implements JobOpeningRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReadJobOpeningResponseDto> findAllByCondition(
            ReadJobOpeningRequestDto requestDto, Pageable pageable
    ) {
        List<ReadJobOpeningResponseDto> dtoList = queryFactory
            .select(Projections.constructor(
                ReadJobOpeningResponseDto.class,
                data.id,
                data.company,
                data.hiringStartPeriod,
                data.hiringEndPeriod,
                data.position
            ))
            .from(data)
            .leftJoin(data.dataKeywords, dataKeyword) // 데이터와 키워드 테이블 조인
            .leftJoin(dataKeyword.keyword, keyword)
            .where(
                eqRequiredSkill(requestDto.getRequiredSkill()),
                eqEducation(requestDto.getEducation()),
                geoHiringStartPeriod(requestDto.getHiringStartPeriod()),
                leoHiringEndPeriod(requestDto.getHiringEndPeriod()),
                eqLocation(requestDto.getLocation()),
                leoCareer(requestDto.getCareer()),
                eqJobType(requestDto.getJobType())
            )
            .groupBy(data.id)
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        log.info("dtoList size: " + dtoList.size());

        List<Long> dataIdList = dtoList.stream()
                .map(ReadJobOpeningResponseDto::getId)
                .toList();

        log.info("dataIdList size: " + dataIdList.size());

        List<Tuple> requiredSkillTupleList = queryFactory
                .select(dataKeyword.data.id, keyword.name)
                .from(dataKeyword)
                .leftJoin(dataKeyword.keyword, keyword)
                .where(dataKeyword.data.id.in(dataIdList))
                .fetch();

        log.info("requiredSkillTupleList size: " + requiredSkillTupleList.size());

        Map<Long, List<String>> requiredSkillMap = requiredSkillTupleList.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(dataKeyword.data.id),
                        Collectors.mapping(tuple -> tuple.get(keyword.name), Collectors.toList())
                ));

        log.info("requiredSkillMap size: " + requiredSkillMap.size());

        dtoList.forEach(dto -> dto.addRequiredSkills(requiredSkillMap.getOrDefault(dto.getId(), new ArrayList<>())));

        Long totalCount = Optional.ofNullable(
            queryFactory.select(Wildcard.count)
                .from(data)
                .leftJoin(data.dataKeywords, dataKeyword)
                .leftJoin(dataKeyword.keyword, keyword)
                .where(
                    eqRequiredSkill(requestDto.getRequiredSkill()),
                    eqEducation(requestDto.getEducation()),
                    geoHiringStartPeriod(requestDto.getHiringStartPeriod()),
                    leoHiringEndPeriod(requestDto.getHiringEndPeriod()),
                    eqLocation(requestDto.getLocation()),
                    leoCareer(requestDto.getCareer()),
                    eqJobType(requestDto.getJobType())
                ).fetchOne())
                .orElse(0L);

        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    private BooleanExpression eqRequiredSkill(String requiredSkill) {
        return requiredSkill != null ? keyword.name.eq(requiredSkill) : Expressions.asBoolean(true).isTrue();
    }

    // 입력된 학력과 같은 데이터만 가져오도록 하는 메서드
    private BooleanExpression eqEducation(String education) {
        return education != null ? data.education.eq(education) : Expressions.asBoolean(true).isTrue();
    }

    // 입력된 시작 날짜보다 큰 데이터만 가져오도록 하는 메서드
    private BooleanExpression geoHiringStartPeriod(LocalDateTime hiringStartPeriod) {
        return hiringStartPeriod != null ? data.hiringStartPeriod.loe(hiringStartPeriod) : Expressions.asBoolean(true).isTrue();
    }

    // 입력된 마감 날짜보다 작은 데이터만 가져오도록 하는 메서드
    private BooleanExpression leoHiringEndPeriod(LocalDateTime hiringEndPeriod) {
        return hiringEndPeriod != null ? data.hiringEndPeriod.loe(hiringEndPeriod) : Expressions.asBoolean(true).isTrue();
    }

    // 동일한 근무 지역의 데이터만 가져오도록 하는 메서드
    private BooleanExpression eqLocation(String location) {
        return location != null ? data.location.eq(location) : Expressions.asBoolean(true).isTrue();
    }

    // 입력된 경력 이하의 데이터만 가져오도록 하는 메서드
    private BooleanExpression leoCareer(Integer career) {
        return career != null ? data.career.loe(career) : Expressions.asBoolean(true).isTrue();
    }

    // 입력된 근무 형태와 동일한 데이터만 가져오도록 하는 메서드
    private BooleanExpression eqJobType(String jobType) {
        return jobType != null ? data.jobType.eq(jobType) : Expressions.asBoolean(true).isTrue();
    }
}
