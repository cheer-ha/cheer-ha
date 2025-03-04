package com.project.cheerha.domain.jobopening.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.project.cheerha.domain.jobopening.elasticsearch.IndexName;
import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.ElasticsearchQueryException;
import com.project.cheerha.domain.history.service.HistoryService;
import com.project.cheerha.domain.jobopening.dto.request.ReadJobOpeningRequestDto;
import com.project.cheerha.domain.jobopening.elasticsearch.dto.response.ReadJobOpeningElasticResponseDto;
import com.project.cheerha.domain.jobopening.elasticsearch.entity.JobOpeningDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobOpeningDocumentService {

    private static final int MAX_JOP_OPENING_SIZE = 10000;  // 최대 페이지 크기 (10000)
    private static final int MAX_POPULAR_SIZE = 100; // 인기 채용공고 최대 개수 (100)

    private final HistoryService historyService;
    private final ElasticsearchClient elasticsearchClient;

    /**
     * Elasticsearch를 사용하여 전체 채용공고를 조회하는 메서드입니다.
     *
     * 이 메서드는 페이지네이션을 지원하며, 채용공고를 최신순(createdAt Desc)으로 정렬하여 반환합니다.
     * 요청된 페이지 크기와 페이지 번호에 맞춰 Elasticsearch에서 데이터를 조회합니다.
     * 페이지 번호가 최대 페이지 수를 초과하면 마지막 페이지를 반환하도록 설정됩니다.
     *
     * @param pageable 페이지 요청 정보 (페이지 번호, 페이지 크기 등)
     * @return 조회된 채용공고 목록을 포함하는 DTO 페이지 객체
     * @throws ElasticsearchQueryException Elasticsearch 쿼리 실행 실패 시 발생
     */
    @Transactional(readOnly = true)
    public Page<ReadJobOpeningElasticResponseDto> readAllJobOpeningsUsingElasticsearch(Pageable pageable) {

        // 페이지 크기 및 페이지 번호 계산
        int pageSize = Math.min(pageable.getPageSize(), MAX_JOP_OPENING_SIZE);
        int pageNumber = pageable.getPageNumber();  // 페이지 번호
        int from = pageNumber * pageSize;  // "from" 값 계산 (페이지 번호 * 페이지 크기)

        // 총 페이지 수가 초과되지 않도록 처리
        int totalPages = (int) Math.ceil((double) MAX_JOP_OPENING_SIZE / pageSize); // totalElements = 10000
        if (pageNumber >= totalPages) {
            pageNumber = totalPages - 1;  // 페이지 번호가 totalPages보다 크면 마지막 페이지로 설정
            from = pageNumber * pageSize;  // 새로운 offset 계산
        }

        // Elasticsearch 쿼리 실행
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(IndexName.JOB_OPENING_DOCUMENT)
                .query(q -> q.matchAll(m -> m))  // 모든 문서 가져오기
                .sort(s -> s.field(f -> f.field(IndexName.CREATED_AT).order(SortOrder.Desc)))  // 최신 등록순으로 정렬
                .from(from)  // 페이지네이션을 위한 "from" 값
                .size(pageSize)  // 페이지 크기만큼 데이터 가져오기
                .build();

        try {
            // Elasticsearch 쿼리 실행
            SearchResponse<JobOpeningDocument> searchResponse = elasticsearchClient.search(searchRequest, JobOpeningDocument.class);
            List<JobOpeningDocument> jobOpeningDocumentList = searchResponse.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());

            // 반환된 결과를 DTO로 매핑
            List<ReadJobOpeningElasticResponseDto> dtoList = jobOpeningDocumentList.stream()
                    .map(job -> new ReadJobOpeningElasticResponseDto(
                            job.getId(),
                            job.getTitle(),
                            job.getCompany(),
                            job.getLocation(),
                            job.getSalary(),
                            job.getEmploymentType(),
                            job.getEducationLevel(),
                            job.getJobOpeningUrl(),
                            job.getMinExperienceYears(),
                            job.getMaxExperienceYears(),
                            job.getPosition(),
                            job.getHiringStartAt(),
                            job.getHiringEndAt(),
                            job.getCreatedAt(),
                            job.getViewCount(),
                            job.getRequiredSkills()
                    ))
                    .collect(Collectors.toList());
            return new PageImpl<>(dtoList, pageable, searchResponse.hits().total().value());

        } catch (IOException e) {
            throw new ElasticsearchQueryException(DataErrorCode.JOB_OPENING_NOT_FOUND);
        }
    }

    /**
     * 조회수 기준으로 상위 100개 인기 채용공고를 조회하는 메서드입니다.
     *
     * 이 메서드는 Elasticsearch에서 조회수를 기준으로 내림차순 정렬하여, 인기 채용공고 100개를 조회합니다.
     * 페이지네이션을 지원하되, 실제로 조회하는 데이터는 최대 100개로 제한됩니다.
     * 페이지 번호가 100개를 초과하면 마지막 페이지를 반환하도록 처리됩니다.
     *
     * @param pageable 페이지 요청 정보 (페이지 번호, 페이지 크기 등)
     * @return 조회수가 많은 상위 100개의 인기 채용공고 목록을 포함하는 페이지 객체
     * @throws ElasticsearchQueryException Elasticsearch 쿼리 실행 실패 시 발생
     */
    @Transactional(readOnly = true)
    public Page<ReadJobOpeningElasticResponseDto> readTop100PopularJobOpeningsUsingElasticsearch(Pageable pageable) {
        // 페이지 크기를 100개로 제한 (사용자가 요청한 크기가 100보다 크다면 100으로 제한)
        int pageSize = Math.min(pageable.getPageSize(), MAX_POPULAR_SIZE);  // 최대 100개까지 가져오기
        int pageNumber = pageable.getPageNumber();  // 페이지 번호
        int from = pageNumber * pageSize;  // Elasticsearch에서 가져올 시작 인덱스 계산

        // 총 페이지 수가 초과되지 않도록 처리
        int totalPages = (int) Math.ceil((double) MAX_POPULAR_SIZE / pageSize); // totalElements = 100
        if (pageNumber >= totalPages) {
            pageNumber = totalPages - 1;  // 페이지 번호가 totalPages보다 크면 마지막 페이지로 설정
            from = pageNumber * pageSize;  // 새로운 offset 계산
        }

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(IndexName.JOB_OPENING_DOCUMENT)  // 상수로 지정된 인덱스 이름
                .query(q -> q.matchAll(m -> m))  // 모든 문서를 가져옴
                .sort(s -> s.field(f -> f.field(IndexName.VIEW_COUNT).order(SortOrder.Desc)))  // 조회수 기준으로 내림차순 정렬
                .from(from)  // 페이지네이션을 위한 from 값 설정
                .size(pageSize)  // 요청한 size 만큼 가져오기
                .build();

        try {
            // Elasticsearch 쿼리 실행
            SearchResponse<JobOpeningDocument> searchResponse = elasticsearchClient.search(searchRequest, JobOpeningDocument.class);
            List<JobOpeningDocument> jobOpeningDocuments = searchResponse.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());

            // 조회된 결과를 DTO로 변환하여 반환
            List<ReadJobOpeningElasticResponseDto> dtoList = jobOpeningDocuments.stream()
                    .map(job -> new ReadJobOpeningElasticResponseDto(
                            job.getId(),
                            job.getTitle(),
                            job.getCompany(),
                            job.getLocation(),
                            job.getSalary(),
                            job.getEmploymentType(),
                            job.getEducationLevel(),
                            job.getJobOpeningUrl(),
                            job.getMinExperienceYears(),
                            job.getMaxExperienceYears(),
                            job.getPosition(),
                            job.getHiringStartAt(),
                            job.getHiringEndAt(),
                            job.getCreatedAt(),
                            job.getViewCount(),
                            job.getRequiredSkills()
                    )).collect(Collectors.toList());
            return new PageImpl<>(dtoList, pageable, Math.min(MAX_POPULAR_SIZE, searchResponse.hits().total().value()));  // Ensure total elements are capped at 100

        } catch (IOException e) {
            log.error("Elasticsearch 조회 실패", e);
            throw new ElasticsearchQueryException(DataErrorCode.JOB_OPENING_NOT_FOUND);
        }
    }

    /**
     * 사용자가 제공한 필터 조건과 검색어를 기반으로 Elasticsearch에서 채용 공고 데이터를 조회하는 메서드입니다.
     *
     * 특정 필터 조건이 존재하면 해당 조건을 Elasticsearch의 BoolQuery에 추가하여 원하는 데이터만 조회할 수 있습니다.
     * 필터 조건이 하나도 추가되지 않은 경우, 전체 채용 공고 데이터를 조회합니다.
     * 검색 결과는 최신순으로 정렬하여 반환됩니다.
     *
     * @param requestDto 필터링 및 검색 조건을 포함한 요청 DTO
     * @param userId 현재 로그인한 사용자의 ID
     * @param pageable 페이지네이션 정보 (페이지 번호 및 페이지 크기)
     * @return 필터링된 채용 공고 목록을 페이지네이션 형태로 반환
     */
    @Transactional
    public Page<ReadJobOpeningElasticResponseDto> readJobOpeningUsingElasticSearchFilter(
            ReadJobOpeningRequestDto requestDto,
            Long userId,
            Pageable pageable
    ) {
        if (requestDto.getSearchTerm() != null) {
            historyService.saveSearchTerm(userId, requestDto.getSearchTerm());
        }

        // BoolQuery를 사용하여 필터링 조건을 설정
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // 지역 필터링
        if (requestDto.getLocation() != null) {
            boolQueryBuilder.filter(f -> f
                    .term(t -> t
                            .field(IndexName.LOCATION)  // 변경된 부분
                            .value(requestDto.getLocation())
                    )
            );
        }

        // 고용 형태 필터링
        if (requestDto.getEmploymentType() != null) {
            boolQueryBuilder.filter(f -> f
                    .term(t -> t
                            .field(IndexName.EMPLOYMENT_TYPE)  // 변경된 부분
                            .value(requestDto.getEmploymentType())
                    )
            );
        }

        // 학력 필터링
        if (requestDto.getEducationLevel() != null) {
            boolQueryBuilder.filter(f -> f
                    .term(t -> t
                            .field(IndexName.EDUCATION_LEVEL)  // 변경된 부분
                            .value(requestDto.getEducationLevel())
                    )
            );
        }

        // 최소 경력 필터링
        if (requestDto.getMinExperienceYears() != null) {
            boolQueryBuilder.filter(f -> f
                    .range(RangeQuery.of(r -> r
                            .term(t -> t
                                    .field(IndexName.MIN_EXPERIENCE_YEARS)  // 변경된 부분
                                    .gte(String.valueOf(requestDto.getMinExperienceYears()))
                            )
                    ))
            );
        }

        // 최대 경력 필터링
        if (requestDto.getMaxExperienceYears() != null) {
            boolQueryBuilder.filter(f -> f
                    .range(RangeQuery.of(r -> r
                            .term(t -> t
                                    .field(IndexName.MAX_EXPERIENCE_YEARS)  // 변경된 부분
                                    .lte(String.valueOf(requestDto.getMaxExperienceYears()))
                            )
                    ))
            );
        }

        // 채용 시작 날짜 필터링
        if (requestDto.getHiringStartAt() != null) {
            boolQueryBuilder.filter(f -> f
                    .range(RangeQuery.of(r -> r
                            .term(t -> t
                                    .field(IndexName.HIRING_START_AT)  // 변경된 부분
                                    .gte(String.valueOf(requestDto.getHiringStartAt()))
                            )
                    ))
            );
        }

        // 채용 마감 날짜 필터링
        if (requestDto.getHiringEndAt() != null) {
            boolQueryBuilder.filter(f -> f
                    .range(RangeQuery.of(r -> r
                            .term(t -> t
                                    .field(IndexName.HIRING_END_AT)  // 변경된 부분
                                    .lte(String.valueOf(requestDto.getHiringEndAt()))
                            )
                    ))
            );
        }

        // 자격 요건 필터링
        if (requestDto.getRequiredSkill() != null) {
            boolQueryBuilder.filter(f -> f
                    .term(t -> t
                            .field(IndexName.REQUIRED_SKILLS + ".keyword")  // 변경된 부분
                            .value(requestDto.getRequiredSkill())
                    )
            );
        }

        // 검색어 조회
        if (requestDto.getSearchTerm() != null) {
            boolQueryBuilder.must(m -> m
                    .match(mq -> mq
                            .field(IndexName.TITLE)  // 변경된 부분
                            .query(requestDto.getSearchTerm())
                    )
            );
        }

        // Elasticsearch 검색 요청 객체 생성
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(IndexName.JOB_OPENING_DOCUMENT)  // 변경된 부분
                .query(q -> q.bool(boolQueryBuilder.build()))
                .sort(s -> s.field(f -> f.field(IndexName.CREATED_AT).order(SortOrder.Desc)))  // 변경된 부분
                .from((int) pageable.getOffset())
                .size(pageable.getPageSize())
                .build();

        try {
            // Elasticsearch 검색 요청 실행
            SearchResponse<JobOpeningDocument> searchResponse = elasticsearchClient.search(searchRequest, JobOpeningDocument.class);

            // 검색 결과에서 문서 리스트 추출
            List<JobOpeningDocument> jobOpeningDocuments = searchResponse.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());

            return new PageImpl<>(jobOpeningDocuments.stream()
                    .map(job -> new ReadJobOpeningElasticResponseDto(
                            job.getId(),
                            job.getTitle(),
                            job.getCompany(),
                            job.getLocation(),
                            job.getSalary(),
                            job.getEmploymentType(),
                            job.getEducationLevel(),
                            job.getJobOpeningUrl(),
                            job.getMinExperienceYears(),
                            job.getMaxExperienceYears(),
                            job.getPosition(),
                            job.getHiringStartAt(),
                            job.getHiringEndAt(),
                            job.getCreatedAt(),
                            job.getViewCount(),
                            job.getRequiredSkills()
                    )).collect(Collectors.toList()), pageable, jobOpeningDocuments.size());

        } catch (IOException e) {
            log.error("Elasticsearch 조회 실패", e);
            throw new ElasticsearchQueryException(DataErrorCode.JOB_OPENING_NOT_FOUND);
        }
    }
}
