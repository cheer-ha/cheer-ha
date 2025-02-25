package com.project.cheerha.domain.jobopening.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.ElasticsearchQueryException;
import com.project.cheerha.domain.history.service.HistoryService;
import com.project.cheerha.domain.jobopening.dto.request.ReadJobOpeningRequestDto;
import com.project.cheerha.domain.jobopening.dto.response.ReadJobOpeningElasticResponseDto;
import com.project.cheerha.domain.jobopening.dto.response.ReadJobOpeningResponseDto;
import com.project.cheerha.domain.jobopening.elasticrepository.JobOpeningDocumentRepository;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.entity.JobOpeningDocument;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import com.project.cheerha.domain.viewcount.entity.JobOpeningViewCount;
import com.project.cheerha.domain.viewcount.repository.JobOpeningViewCountRepository;
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
public class JobOpeningService {

    private static final int MAX_JOP_OPENING_SIZE = 10000;  // 최대 페이지 크기 (10000)
    private static final int MAX_POPULAR_SIZE = 100; // 인기 채용공고 최대 개수 (100)
    private static final String INDEX_NAME = "job-opening";  // Elasticsearch 인덱스 이름
    private static final String CREATED_AT_FIELD = "createdAt";  // 정렬할 필드 (createdAt)
    private static final String VIEW_COUNT_FIELD = "viewCount";  // 정렬할 필드 (viewCount)

    private final JobOpeningRepository jobOpeningRepository;
    private final JobOpeningDocumentRepository jobOpeningDocumentRepository;
    private final HistoryService historyService;
    private final UserFindByService userFindByIdService;
    private final JobOpeningFindByService jobOpeningFindByService;
    private final ElasticsearchClient elasticsearchClient;
    private final JobOpeningViewCountRepository jobOpeningViewCountRepository;

    /**
     * 채용공고 리다이렉트 동시성 제어를 위한 집계 테이블 조회수 카운팅 메서드 입니다.
     * viewCount 정보를 관리하는 집계 테이블에서 조회수가 카운팅됩니다.
     * viewcount 테이블에서 비관 락이 작동하여 count 값의 정합성을 유지합니다.
     * @param id 채용공고 식별 id
     */
    @Transactional
    public void increaseViewCount(Long id) {
        JobOpeningViewCount viewCount = jobOpeningViewCountRepository.findByForUpdateViewCount(id)
            .orElseGet(() -> {
                JobOpening jobOpening = jobOpeningFindByService.findById(id);
                return jobOpeningViewCountRepository.save(JobOpeningViewCount.create(jobOpening));
            });
        viewCount.increaseViewCount();
    }

    /**
     * 페이지 리다이렉트를 위한 서비스 로직입니다.
     * @param jobOpening
     * @return 리다이렉트 될 페이지 URL
     */
    public String getJobOpeningUrl(JobOpening jobOpening) {
        String url = jobOpening.getJobOpeningUrl();
        if (!url.startsWith("http")) {
            url = "https://" + url;
        }
        return url;
    }


    /**
     * 채용 공고 목록을 조회하는 메서드입니다.
     *
     * requestDto에 값이 포함되면 해당 조건을 기준으로 필터링이 적용됩니다.
     * searchTerm이 존재하면 해당 검색어를 Redis에 저장합니다.
     *
     * @param requestDto 조회 조건을 포함한 요청 Dto
     * @param userId 검색어 저장을 위한 유저의 Id
     * @param pageable 페이지 요청 정보 (페이지 번호, 페이지 크기)
     * @return 필터링된 채용 공고 목록
     */
    @Transactional
    public Page<ReadJobOpeningResponseDto> readJobOpenings(
            ReadJobOpeningRequestDto requestDto,
            Long userId,
            Pageable pageable
    ) {
        User user = userFindByIdService.findById(userId);

        if (requestDto.getSearchTerm() != null) {
            historyService.saveSearchTerm(userId, requestDto.getSearchTerm());
        }

        Page<ReadJobOpeningResponseDto> dtoPage = jobOpeningRepository.findAllByCondition(
                requestDto, pageable);

        return dtoPage;
    }

    /**
     * 조회수 기준으로 상위 100개의 인기 채용공고를 조회하는 메서드입니다.
     *
     * 이 메서드는 `jobOpeningRepositoryQuery`를 사용하여 조회수를 내림차순으로 정렬한 후,
     * 인기 채용공고 100개를 반환합니다.
     *
     * 페이지네이션을 지원하지만, 실제로는 상위 100개만 조회하므로 페이지 크기(size)는 100으로 고정됩니다.
     *
     * @param pageable 페이지 요청 정보 (페이지 번호, 페이지 크기 등)
     * @return 조회수가 많은 상위 100개의 인기 채용공고 목록을 포함하는 페이지 객체
     */
    @Transactional(readOnly = true)
    public Page<ReadJobOpeningResponseDto> readTop100PopularJobOpenings(Pageable pageable) {
        // 인기 채용공고 조회
        Page<ReadJobOpeningResponseDto> dtoPage = jobOpeningRepository.findTop100PopularJobOpenings(pageable);

        // 조회된 채용공고 DTO에 requiredSkills 추가
        for (ReadJobOpeningResponseDto dto : dtoPage) {
            JobOpening jobOpening = jobOpeningFindByService.findById(dto.getId());
            if (jobOpening != null) {
                // JobOpening에서 requiredSkills를 가져오기
                List<String> requiredSkillList = jobOpening.getRequiredSkillList();
                dto.addRequiredSkills(requiredSkillList); // DTO에 requiredSkills 추가
            }
        }

        return dtoPage;
    }


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
                .index(INDEX_NAME)  // 상수로 지정된 인덱스 이름
                .query(q -> q.matchAll(m -> m))  // 모든 문서 가져오기
                .sort(s -> s.field(f -> f.field(CREATED_AT_FIELD).order(SortOrder.Desc)))  // 최신 등록순으로 정렬
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
                .index(INDEX_NAME)  // 상수로 지정된 인덱스 이름
                .query(q -> q.matchAll(m -> m))  // 모든 문서를 가져옴
                .sort(s -> s.field(f -> f.field(VIEW_COUNT_FIELD).order(SortOrder.Desc)))  // 조회수 기준으로 내림차순 정렬
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
}
