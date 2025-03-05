package com.project.cheerha.domain.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.project.cheerha.common.exception.data.ElasticsearchQueryException;
import com.project.cheerha.domain.elasticsearch.IndexName;
import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.domain.elasticsearch.dto.request.ReadJobOpeningElasticRequestDto;
import com.project.cheerha.domain.elasticsearch.dto.response.ReadJobOpeningElasticResponseDto;
import com.project.cheerha.domain.elasticsearch.entity.JobOpeningDocument;
import com.project.cheerha.domain.elasticsearch.filter.JobOpeningDocumentFilter;
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

    private final ElasticsearchClient elasticsearchClient;

    /**
     * Elasticsearch를 사용하여 전체 채용공고를 조회하는 메서드입니다.
     *
     * @param pageable 페이지 요청 정보 (페이지 번호, 페이지 크기 등)
     * @return 조회된 채용공고 목록을 포함하는 DTO 페이지 객체
     * @throws ElasticsearchQueryException Elasticsearch 쿼리 실행 실패 시 발생
     */
    @Transactional(readOnly = true)
    public Page<ReadJobOpeningElasticResponseDto> readAllJobOpeningsUsingElasticsearch(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int from = calculateFrom(pageNumber, pageSize, IndexName.MAX_JOP_OPENING_SIZE);
        SearchRequest searchRequest = buildSearchRequest(from, pageSize, null);
        try {
            // Elasticsearch 쿼리 실행
            SearchResponse<JobOpeningDocument> searchResponse = elasticsearchClient.search(searchRequest, JobOpeningDocument.class);
            List<JobOpeningDocument> jobOpeningDocumentList = searchResponse.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());
            // 반환할 DTO 리스트로 변환
            List<ReadJobOpeningElasticResponseDto> dtoList = ReadJobOpeningElasticResponseDto.toDto(jobOpeningDocumentList);
            return new PageImpl<>(dtoList, pageable, IndexName.MAX_JOP_OPENING_SIZE);
        } catch (IOException e) {
            throw new ElasticsearchQueryException(DataErrorCode.JOB_OPENING_NOT_FOUND);
        }
    }

    /**
     * 조회수 기준으로 상위 100개 인기 채용공고를 조회하는 메서드입니다.
     *
     * @param pageable 페이지 요청 정보 (페이지 번호, 페이지 크기 등)
     * @return 조회수가 많은 상위 100개의 인기 채용공고 목록을 포함하는 페이지 객체
     * @throws ElasticsearchQueryException Elasticsearch 쿼리 실행 실패 시 발생
     */
    @Transactional(readOnly = true)
    public Page<ReadJobOpeningElasticResponseDto> readTop100PopularJobOpeningsUsingElasticsearch(Pageable pageable) {
        int pageSize = Math.min(pageable.getPageSize(), IndexName.MAX_POPULAR_SIZE);
        int pageNumber = pageable.getPageNumber();
        int from = calculateFrom(pageNumber, pageSize, IndexName.MAX_POPULAR_SIZE);
        SearchRequest searchRequest = buildSearchRequest(from, pageSize, IndexName.VIEW_COUNT);
        try {
            SearchResponse<JobOpeningDocument> searchResponse = elasticsearchClient.search(searchRequest, JobOpeningDocument.class);
            List<JobOpeningDocument> jobOpeningDocuments = searchResponse.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());
            List<ReadJobOpeningElasticResponseDto> dtoList = ReadJobOpeningElasticResponseDto.toDto(jobOpeningDocuments);
            return new PageImpl<>(dtoList, pageable, IndexName.MAX_POPULAR_SIZE);
        } catch (IOException e) {
            log.error("Elasticsearch 조회 실패", e);
            throw new ElasticsearchQueryException(DataErrorCode.JOB_OPENING_NOT_FOUND);
        }
    }

    /**
     * 사용자가 제공한 필터 조건과 검색어를 기반으로 Elasticsearch에서 채용 공고 데이터를 조회하는 메서드입니다.
     *
     * @param requestDto 필터링 및 검색 조건을 포함한 요청 DTO
     * @param userId 현재 로그인한 사용자의 ID
     * @param pageable 페이지네이션 정보 (페이지 번호 및 페이지 크기)
     * @return 필터링된 채용 공고 목록을 페이지네이션 형태로 반환
     */
    @Transactional
    public Page<ReadJobOpeningElasticResponseDto> readJobOpeningUsingElasticSearchFilter(
            ReadJobOpeningElasticRequestDto requestDto,
            Long userId,
            Pageable pageable
    ) {
        JobOpeningDocumentFilter filter = new JobOpeningDocumentFilter(requestDto);
        var boolQueryBuilder = filter.build();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int from = calculateFrom(pageNumber, pageSize, IndexName.MAX_JOP_OPENING_SIZE);
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(IndexName.JOB_OPENING_DOCUMENT)
                .query(q -> q.bool(boolQueryBuilder.build()))
                .sort(s -> s.field(f -> f.field(IndexName.CREATED_AT).order(SortOrder.Desc)))
                .from(from)
                .size(pageSize)
                .build();
        try {
            SearchResponse<JobOpeningDocument> searchResponse = elasticsearchClient.search(searchRequest, JobOpeningDocument.class);
            List<JobOpeningDocument> jobOpeningDocuments = searchResponse.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());
            List<ReadJobOpeningElasticResponseDto> dtoList = ReadJobOpeningElasticResponseDto.toDto(jobOpeningDocuments);
            return new PageImpl<>(dtoList, pageable, IndexName.MAX_JOP_OPENING_SIZE);
        } catch (IOException e) {
            log.error("Elasticsearch 조회 실패", e);
            throw new ElasticsearchQueryException(DataErrorCode.JOB_OPENING_NOT_FOUND);
        }
    }

    /**
     * 페이지 번호와 페이지 크기를 바탕으로 Elasticsearch의 "from" 값을 계산하는 메서드
     */
    private int calculateFrom(int pageNumber, int pageSize, int maxSize) {
        int totalPages = (int) Math.ceil((double) maxSize / pageSize);
        if (pageNumber >= totalPages) {
            pageNumber = totalPages - 1;
        }
        return pageNumber * pageSize;
    }

    /**
     * Elasticsearch의 쿼리 요청을 설정하는 메서드
     */
    private SearchRequest buildSearchRequest(int from, int pageSize, String sortField) {
        SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder()
                .index(IndexName.JOB_OPENING_DOCUMENT)
                .from(from)
                .size(pageSize);
        if (sortField != null) {
            searchRequestBuilder.sort(s -> s.field(f -> f.field(sortField).order(SortOrder.Desc)));
        }
        // 마감된 채용공고는 조회되지 않도록 처리
        filterExpiredJobOpenings(searchRequestBuilder);
        return searchRequestBuilder.build();
    }

    /**
     * 마감된 채용공고를 조회되지 않도록 필터링하는 메서드
     */
    private void filterExpiredJobOpenings(SearchRequest.Builder searchRequestBuilder) {
        searchRequestBuilder.query(q -> q
                .range(RangeQuery.of(r -> r
                                .term(t -> t
                                        .field(IndexName.HIRING_END_AT)
                                        .gte("now")
                                )
                        )
                )
        );
    }
}
