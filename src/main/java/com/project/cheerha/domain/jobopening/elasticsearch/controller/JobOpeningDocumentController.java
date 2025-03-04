package com.project.cheerha.domain.jobopening.elasticsearch.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.ApiResponseDto;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.jobopening.dto.request.ReadJobOpeningRequestDto;
import com.project.cheerha.domain.jobopening.elasticsearch.dto.response.ReadJobOpeningElasticResponseDto;
import com.project.cheerha.domain.jobopening.elasticsearch.service.JobOpeningDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/job-opening")
@RestController
@RequiredArgsConstructor
public class JobOpeningDocumentController {

    private final JobOpeningDocumentService jobOpeningDocumentService;

    private Pageable validatePageSize(int page, int size) {
        if (page < 1 || size < 1) {
            page = Math.max(1, page);
            size = Math.max(1, size);
        }
        return PageRequest.of(page - 1, size);
    }

    /**
     * Elasticsearch를 사용하여 모든 채용 공고를 조회하는 메서드입니다.
     *
     * Elasticsearch에서 모든 채용 공고를 검색하고, 결과를 DTO로 반환합니다.
     * 페이지네이션을 지원하여, 필요한 페이지 범위의 데이터만을 조회합니다.
     *
     * @param page 조회할 페이지 번호 (기본값: 1)
     * @param size 페이지 당 조회할 데이터 수 (기본값: 10)
     * @return Elasticsearch에서 조회된 채용 공고 목록을 포함한 API 응답 객체
     */
    @GetMapping("/search/elastic")
    public ResponseEntity<ApiResponseDto<Page<ReadJobOpeningElasticResponseDto>>> readJobOpeningsUsingElasticsearch(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = validatePageSize(page, size);
        Page<ReadJobOpeningElasticResponseDto> dtoList = jobOpeningDocumentService.readAllJobOpeningsUsingElasticsearch(pageable);
        return ApiResponseDto.success(dtoList);
    }

    /**
     * 조회수 기준으로 인기 채용 공고를 Elasticsearch에서 조회하는 메서드입니다.
     *
     * 이 API는 조회수 기준으로 내림차순 정렬된 인기 채용 공고를 100개까지 조회합니다.
     * 페이지네이션을 지원하며, 페이지 번호와 크기를 매개변수로 전달받습니다.
     *
     * @param page 조회할 페이지 번호 (기본값: 1)
     * @param size 페이지 당 조회할 데이터 수 (기본값: 10)
     * @return Elasticsearch에서 조회된 인기 채용 공고 목록을 포함한 API 응답 객체
     */
    @GetMapping("/popular/elastic")
    public ResponseEntity<ApiResponseDto<Page<ReadJobOpeningElasticResponseDto>>> readTop100PopularJobOpeningsUsingElasticsearch(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = validatePageSize(page, size);
        Page<ReadJobOpeningElasticResponseDto> dtoPage = jobOpeningDocumentService.readTop100PopularJobOpeningsUsingElasticsearch(pageable);
        return ApiResponseDto.success(dtoPage);
    }

    /**
     * 전체 채용 공고 데이터와 필터링 및 검색어를 통해 Elasticsearch에서 데이터를 조회하는 API입니다.
     *
     * @param requestDto 사용자가 입력한 필터링 조건을 포함한 DTO
     * @param authUser 현재 로그인한 사용자의 정보
     * @param page 조회할 페이지 번호
     * @param size 페이지 당 조회할 데이터 수
     * @return 필터링된 채용 공고 목록을 페이지네이션 형태로 반환
     */
    @GetMapping("/search/elastic/filters")
    public ResponseEntity<ApiResponseDto<Page<ReadJobOpeningElasticResponseDto>>> readJobOpeningElasticsearch(
        @ModelAttribute ReadJobOpeningRequestDto requestDto,
        @Auth AuthUser authUser,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = validatePageSize(page, size);
        Long userId = authUser.id();

        Page<ReadJobOpeningElasticResponseDto> jobOpeningElasticResponseDtoPage = jobOpeningDocumentService.readJobOpeningUsingElasticSearchFilter(requestDto, userId, pageable);

        return ApiResponseDto.success(jobOpeningElasticResponseDtoPage);
    }
}
