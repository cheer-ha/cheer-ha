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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.cheerha.domain.jobOpening.entity.QJobOpening.jobOpening;
import static com.project.cheerha.domain.keyword.entity.QJobOpeningKeyword.jobOpeningKeyword;
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
                jobOpening.id,
                jobOpening.company,
                jobOpening.hiringStartAt,
                jobOpening.hiringEndAt,
                jobOpening.position
            ))
            .from(jobOpening)
            .leftJoin(jobOpening.jobOpeningKeywordList, jobOpeningKeyword) // 데이터와 키워드 테이블 조인
            .leftJoin(jobOpeningKeyword.keyword, keyword)
            .where(
                eqRequiredSkill(requestDto.getRequiredSkill()),
                eqEducation(requestDto.getEducationLevel()),
                geoHiringStartPeriod(requestDto.getHiringStartAt()),
                leoHiringEndPeriod(requestDto.getHiringEndAt()),
                eqLocation(requestDto.getLocation()),
                leoCareer(requestDto.getExperienceYears()),
                eqJobType(requestDto.getEmploymentType()),
                containsSearchTerm(requestDto.getSearchTerm())
            )
            .groupBy(jobOpening.id)
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        log.info("dtoList size: " + dtoList.size());

        List<Long> dataIdList = dtoList.stream()
                .map(ReadJobOpeningResponseDto::getId)
                .toList();

        log.info("dataIdList size: " + dataIdList.size());

        List<Tuple> requiredSkillTupleList = queryFactory
                .select(jobOpeningKeyword.jobOpening.id, keyword.name)
                .from(jobOpeningKeyword)
                .leftJoin(jobOpeningKeyword.keyword, keyword)
                .where(jobOpeningKeyword.jobOpening.id.in(dataIdList))
                .fetch();

        log.info("requiredSkillTupleList size: " + requiredSkillTupleList.size());

        Map<Long, List<String>> requiredSkillMap = requiredSkillTupleList.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(jobOpeningKeyword.jobOpening.id),
                        Collectors.mapping(tuple -> tuple.get(keyword.name), Collectors.toList())
                ));

        log.info("requiredSkillMap size: " + requiredSkillMap.size());

        dtoList.forEach(dto -> dto.addRequiredSkills(requiredSkillMap.getOrDefault(dto.getId(), new ArrayList<>())));

        Long totalCount = Optional.ofNullable(
            queryFactory.select(Wildcard.count)
                .from(jobOpening)
                .leftJoin(jobOpening.jobOpeningKeywordList, jobOpeningKeyword)
                .leftJoin(jobOpeningKeyword.keyword, keyword)
                .where(
                    eqRequiredSkill(requestDto.getRequiredSkill()),
                    eqEducation(requestDto.getEducationLevel()),
                    geoHiringStartPeriod(requestDto.getHiringStartAt()),
                    leoHiringEndPeriod(requestDto.getHiringEndAt()),
                    eqLocation(requestDto.getLocation()),
                    leoCareer(requestDto.getExperienceYears()),
                    eqJobType(requestDto.getEmploymentType()),
                    containsSearchTerm(requestDto.getSearchTerm())
                ).fetchOne())
                .orElse(0L);

        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    // 입력된 자격 스킬을 포함한 데이터만 가져오도록 하는 메서드
    private BooleanExpression eqRequiredSkill(String requiredSkill) {
        return requiredSkill != null ? keyword.name.eq(requiredSkill) : Expressions.asBoolean(true).isTrue();
    }

    // 입력된 학력과 같은 데이터만 가져오도록 하는 메서드
    private BooleanExpression eqEducation(String educationLevel) {
        return educationLevel != null ? jobOpening.educationLevel.eq(educationLevel) : Expressions.asBoolean(true).isTrue();
    }

    // 입력된 시작 날짜보다 큰 데이터만 가져오도록 하는 메서드
    private BooleanExpression geoHiringStartPeriod(ZonedDateTime hiringStartAt) {
        return hiringStartAt != null ? jobOpening.hiringStartAt.loe(hiringStartAt) : Expressions.asBoolean(true).isTrue();
    }

    // 입력된 마감 날짜보다 작은 데이터만 가져오도록 하는 메서드
    private BooleanExpression leoHiringEndPeriod(ZonedDateTime hiringEndAt) {
        return hiringEndAt != null ? jobOpening.hiringEndAt.loe(hiringEndAt) : Expressions.asBoolean(true).isTrue();
    }

    // 동일한 근무 지역의 데이터만 가져오도록 하는 메서드
    private BooleanExpression eqLocation(String location) {
        return location != null ? jobOpening.location.eq(location) : Expressions.asBoolean(true).isTrue();
    }

    // 입력된 경력 이하의 데이터만 가져오도록 하는 메서드
    private BooleanExpression leoCareer(Integer maxExperienceYears) {
        return maxExperienceYears != null ? jobOpening.maxExperienceYears.loe(maxExperienceYears) : Expressions.asBoolean(true).isTrue();
    }

    // 입력된 근무 형태와 동일한 데이터만 가져오도록 하는 메서드
    private BooleanExpression eqJobType(String EmploymentType) {
        return EmploymentType != null ? jobOpening.employmentType.eq(EmploymentType) : Expressions.asBoolean(true).isTrue();
    }

    // 입력된 사용자 키워드를 포함하는 데이터만 가져오도록 하는 메서드
    private BooleanExpression containsSearchTerm(String searchTerm) {
        return searchTerm != null ? jobOpening.company.containsIgnoreCase(searchTerm) : Expressions.asBoolean(true).isTrue();
    }
}
