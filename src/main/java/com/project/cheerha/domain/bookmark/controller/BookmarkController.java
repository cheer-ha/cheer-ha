package com.project.cheerha.domain.bookmark.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.ApiResponseDto;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.bookmark.dto.request.CreateBookmarkRequestDto;
import com.project.cheerha.domain.bookmark.dto.request.DeleteBookmarkRequestDto;
import com.project.cheerha.domain.bookmark.dto.response.ReadBookmarkResponseDto;
import com.project.cheerha.domain.bookmark.service.BookmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/bookmarks")
@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<String>> createBookmark(
            @Valid @RequestBody CreateBookmarkRequestDto dto,
            @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();
        bookmarkService.createBookmark(userId, dto.jobOpeningId());
        return ApiResponseDto.created("즐겨찾기 추가 완료"); // Return an empty object
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<ReadBookmarkResponseDto>>> readAllBookmarks(
            @Auth AuthUser authUser,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Long userId = authUser.id();
        Page<ReadBookmarkResponseDto> responsePage = bookmarkService.readAllBookmarks(userId, page, size);
        return ApiResponseDto.success(responsePage);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponseDto<Void>> deleteBookmark(
            @Valid @RequestBody DeleteBookmarkRequestDto dto,
            @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();
        bookmarkService.deleteBookmark(userId, dto.jobOpeningId());
        return ApiResponseDto.noContent();
    }
}
