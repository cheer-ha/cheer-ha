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

    /**
     * 주어진 검색 조건을 기반으로 채용 공고 목록을 조회합니다.
     *
     * 다양한 필터 조건을 적용해서 채용 공고 데이터를 가져오고, 해당 공고의 키워드(자격 요건)를 함께 조회합니다.
     *
     * @param requestDto 검색 및 필터링 조건
     * @param pageable 페이지 정보를 포함 (페이지 번호, 페이지 크기)
     * @return 필터링된 채용 공고 목록
     */
    @Override
    public Page<ReadJobOpeningResponseDto> findAllByCondition(
            ReadJobOpeningRequestDto requestDto, Pageable pageable
    ) {
        // 1. 기본 채용 공고 데이터 조회 (필터 조건 적용)
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

        // 2. 조회된 채용 공고 Id 목록 추출
        List<Long> dataIdList = dtoList.stream()
                .map(ReadJobOpeningResponseDto::getId)
                .toList();

        log.info("dataIdList size: " + dataIdList.size());

        // 3. 해당 공고의 자격 요건 조회
        List<Tuple> requiredSkillTupleList = queryFactory
                .select(jobOpeningKeyword.jobOpening.id, keyword.name)
                .from(jobOpeningKeyword)
                .leftJoin(jobOpeningKeyword.keyword, keyword)
                .where(jobOpeningKeyword.jobOpening.id.in(dataIdList))
                .fetch();

        log.info("requiredSkillTupleList size: " + requiredSkillTupleList.size());

        // 4. 채용 공고 Id별 자격 요건 리스트 매핑
        Map<Long, List<String>> requiredSkillMap = requiredSkillTupleList.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(jobOpeningKeyword.jobOpening.id),
                        Collectors.mapping(tuple -> tuple.get(keyword.name), Collectors.toList())
                ));

        log.info("requiredSkillMap size: " + requiredSkillMap.size());

        // 5. 각 Dto에 자격 요건 추가
        dtoList.forEach(dto -> dto.addRequiredSkills(requiredSkillMap.getOrDefault(dto.getId(), new ArrayList<>())));

        // 6. 전체 검색 결과 개수 조회
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

        // 7. 결과 반환
        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    /**
     * 조회수를 기준으로 상위 100개의 인기 채용 공고를 반환합니다.
     *
     * @param pageable 페이지 정보 (100개만 가져오므로 페이지 크기 100으로 설정)
     * @return 조회수가 많은 상위 100개 채용 공고 목록
     */
    @Override
    public Page<ReadJobOpeningResponseDto> findTop100PopularJobOpenings(Pageable pageable) {
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
                .orderBy(jobOpening.viewCount.desc())  // 조회수 내림차순 정렬
                .limit(100)  // 상위 100개만 조회
                .fetch();

        log.info("dtoList size: " + dtoList.size());

        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }

    private BooleanExpression eqRequiredSkill(String requiredSkill) {
        return requiredSkill != null ? keyword.name.eq(requiredSkill) : Expressions.asBoolean(true).isTrue();
    }

    private BooleanExpression eqEducation(String educationLevel) {
        return educationLevel != null ? jobOpening.educationLevel.eq(educationLevel) : Expressions.asBoolean(true).isTrue();
    }

    private BooleanExpression geoHiringStartPeriod(ZonedDateTime hiringStartAt) {
        return hiringStartAt != null ? jobOpening.hiringStartAt.goe(hiringStartAt) : Expressions.asBoolean(true).isTrue();
    }

    private BooleanExpression leoHiringEndPeriod(ZonedDateTime hiringEndAt) {
        return hiringEndAt != null ? jobOpening.hiringEndAt.loe(hiringEndAt) : Expressions.asBoolean(true).isTrue();
    }

    private BooleanExpression eqLocation(String location) {
        return location != null ? jobOpening.location.eq(location) : Expressions.asBoolean(true).isTrue();
    }

    private BooleanExpression leoCareer(Integer maxExperienceYears) {
        return maxExperienceYears != null ? jobOpening.maxExperienceYears.loe(maxExperienceYears) : Expressions.asBoolean(true).isTrue();
    }

    private BooleanExpression eqJobType(String EmploymentType) {
        return EmploymentType != null ? jobOpening.employmentType.eq(EmploymentType) : Expressions.asBoolean(true).isTrue();
    }

    private BooleanExpression containsSearchTerm(String searchTerm) {
        return searchTerm != null ? jobOpening.title.containsIgnoreCase(searchTerm) : Expressions.asBoolean(true).isTrue();
    }
}
