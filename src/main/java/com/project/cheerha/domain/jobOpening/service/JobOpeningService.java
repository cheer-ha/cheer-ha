package com.project.cheerha.domain.jobOpening.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.project.cheerha.domain.history.entity.History;
import com.project.cheerha.domain.history.repository.HistoryRepository;
import com.project.cheerha.domain.jobOpening.dto.request.ReadJobOpeningRequestDto;
import com.project.cheerha.domain.jobOpening.dto.response.ReadJobOpeningElasticResponseDto;
import com.project.cheerha.domain.jobOpening.dto.response.ReadJobOpeningResponseDto;
import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import com.project.cheerha.domain.jobOpening.entity.JobOpeningDocument;
import com.project.cheerha.domain.jobOpening.repository.JobOpeningRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class  JobOpeningService {

    private final JobOpeningRepository jobOpeningRepository;
    private final HistoryRepository historyRepository;
    private final UserFindByService userFindByIdService;
    private final JobOpeningFindByService jobOpeningFindByService;
    private final ElasticsearchClient elasticsearchClient;

    public String getJobOpeningUrlAndIncreaseViewCount(Long id) {
        JobOpening jobOpening = jobOpeningFindByService.findById(id);

        String url = jobOpening.getJobOpeningUrl();

        if (!url.startsWith("http")) {
            url = "https://" + url;
        }

        log.info("최종 리다이렉트 URL: {}", url);

        jobOpening.increaseViewCount();
        jobOpeningRepository.save(jobOpening);
        return url;
    }

    @Transactional
    public Page<ReadJobOpeningResponseDto> readJobOpenings(
            ReadJobOpeningRequestDto requestDto,
            Long userId,
            Pageable pageable
    ) {
        User user = userFindByIdService.findById(userId);

        if (requestDto.getSearchTerm() != null) {
            History history = History.toEntity(user, requestDto.getSearchTerm());
            historyRepository.save(history);
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
        return jobOpeningRepository.findTop100PopularJobOpenings(pageable);
    }

    /**
     * Elasticsearch를 사용하여 전체 채용공고를 조회하는 메서드입니다.
     *
     * 이 메서드는 모든 채용공고를 가져와서 DTO로 변환하여 반환합니다.
     *
     * @return 채용공고 목록을 포함하는 DTO 리스트
     * @throws RuntimeException Elasticsearch 쿼리 실행 실패 시 발생
     */
    @Transactional(readOnly = true)
    public List<ReadJobOpeningElasticResponseDto> readAllJobOpeningsUsingElasticsearch() {
        // Elasticsearch에서 쿼리 실행
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("job-opening")
                .query(q -> q.queryString(qs -> qs.query("*"))) // 모든 문서 가져오기
                .build();

        try {
            // Elasticsearch 쿼리 실행
            SearchResponse<JobOpeningDocument> searchResponse = elasticsearchClient.search(searchRequest, JobOpeningDocument.class);

            // 결과 매핑
            List<JobOpeningDocument> jobOpeningDocuments = searchResponse.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());

            // 반환된 결과를 DTO로 매핑
            return jobOpeningDocuments.stream()
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
                            job.getViewCount(),
                            job.getRequiredSkills()
                    )).collect(Collectors.toList());
        } catch (IOException e) {
            // 예외 처리
            throw new RuntimeException("Elasticsearch query failed", e);
        }
    }
}
