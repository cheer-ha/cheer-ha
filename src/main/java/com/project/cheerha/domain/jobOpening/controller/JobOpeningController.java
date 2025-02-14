package com.project.cheerha.domain.jobOpening.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.jobOpening.dto.request.ReadJobOpeningRequestDto;
import com.project.cheerha.domain.jobOpening.dto.response.ReadJobOpeningResponseDto;
import com.project.cheerha.domain.jobOpening.service.JobOpeningService;
import com.project.cheerha.common.dto.ApiResponseDto;
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
public class JobOpeningController {

    private final JobOpeningService jobOpeningService;

    @GetMapping("/{id}")
    public RedirectView getRedirectedView(@PathVariable Long id) {
        String url = jobOpeningService.getJobOpeningUrlAndIncreaseViewCount(id);
        return new RedirectView(url);
    }

    /**
     * 채용 공고 목록을 필터링 및 검색하여 조회하는 API입니다.
     *
     * 사용자는 특정 검색어 및 필터 조건을 설정하여 원하는 채용 공고만 조회할 수 있습니다.
     *
     * @param requestDto 필터링 및 검색어를 포함
     * @param page 조회할 페이지 번호
     * @param size 페이지당 조회할 채용 공고 수
     * @param authUser 현재 로그인한 사용자 정보
     * @return 필터링된 채용 공고 목록 (페이지)
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<Page<ReadJobOpeningResponseDto>>> readJobOpenings(
            @ModelAttribute ReadJobOpeningRequestDto requestDto,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @Auth AuthUser authUser
    ) {
        Pageable pageable = validatePageSize(page, size);
        Long userId = authUser.id();
        Page<ReadJobOpeningResponseDto> dtoPage = jobOpeningService.readJobOpenings(
                requestDto, userId, pageable
        );
        return ApiResponseDto.success(dtoPage);
    }

    private Pageable validatePageSize(int page, int size) {
        if (page < 1 || size < 1) {
            page = Math.max(1, page);
            size = Math.max(1, size);
        }
        return PageRequest.of(page - 1, size);
    }

    /**
     * 조회수를 기준으로 상위 100개의 인기 채용 공고를 조회하는 API입니다.
     *
     * 이 API는 클라이언트가 조회수를 기준으로 가장 인기 있는 채용 공고 100개를 요청할 수 있게 해줍니다.
     * 페이지 번호와 크기를 매개변수로 받아 페이징 처리된 결과를 반환합니다.
     * 페이지 번호나 크기가 1보다 작은 경우 기본값 1로 처리됩니다.
     *
     * @param page 조회할 페이지 번호 (기본값: 1, 1보다 작으면 1로 처리됨)
     * @param size 페이지 당 조회할 데이터 수 (기본값: 100, 1보다 작으면 1로 처리됨)
     * @return 인기 채용 공고 목록을 포함한 응답 객체 (API 응답 형식: ApiResponseDto)
     */
    @GetMapping("/popular")
    public ResponseEntity<ApiResponseDto<Page<ReadJobOpeningResponseDto>>> readTop100PopularJobOpenings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        page = Math.max(1, page);
        size = Math.max(1, size);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ReadJobOpeningResponseDto> dtoPage = jobOpeningService.readTop100PopularJobOpenings(pageable);
        return ApiResponseDto.success(dtoPage);
    }

}
