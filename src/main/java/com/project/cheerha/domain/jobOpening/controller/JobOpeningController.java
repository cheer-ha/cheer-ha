package com.project.cheerha.domain.jobOpening.controller;


import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.jobOpening.dto.request.ReadJobOpeningRequestDto;
import com.project.cheerha.domain.jobOpening.dto.response.ReadJobOpeningResponseDto;
import com.project.cheerha.domain.jobOpening.service.JobOpeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/job-opening")
@RestController
@RequiredArgsConstructor
public class JobOpeningController {

    private final JobOpeningService jobOpeningService;

    @GetMapping("/{id}")
    public RedirectView getRedirectedView (@PathVariable Long id) {
        String url = jobOpeningService.getJobOpeningUrlAndIncreaseViewCount(id);
        return new RedirectView(url);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ReadJobOpeningResponseDto>> readJobOpenings(
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

        return ResponseEntity.status(HttpStatus.OK).body(dtoPage);
    }

    private Pageable validatePageSize(int page, int size) {
        if (page < 1 || size < 1) {
            page = Math.max(1, page);
            size = Math.max(1, size);
        }

        return PageRequest.of(page - 1, size);
    }
}
