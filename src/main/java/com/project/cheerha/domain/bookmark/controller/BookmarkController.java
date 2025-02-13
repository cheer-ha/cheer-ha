package com.project.cheerha.domain.bookmark.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.bookmark.dto.request.CreateBookmarkRequestDto;
import com.project.cheerha.domain.bookmark.dto.request.DeleteBookmarkRequestDto;
import com.project.cheerha.domain.bookmark.dto.response.ReadBookmarkResponseDto;
import com.project.cheerha.domain.bookmark.service.BookmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 로그인된 사용자의 즐겨찾기 추가
    @PostMapping
    public ResponseEntity<Void> createBookmark(
            @Valid @RequestBody CreateBookmarkRequestDto dto,
            @Auth AuthUser authUser
    ) {
        // 이미 인증된 사용자의 id를 가져옵니다.
        Long userId = authUser.id();

        // 북마크 생성
        bookmarkService.createBookmark(userId, dto.jobOpeningId());

        // 즐겨찾기 추가 시 201 응답 코드 리턴
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 로그인된 사용자의 모든 즐겨찾기 조회
    @GetMapping
    public ResponseEntity<Page<ReadBookmarkResponseDto>> readAllBookmarks(
            @Auth AuthUser authUser,
            @RequestParam int page,
            @RequestParam int size
    ) {
        // 이미 인증된 사용자의 id를 가져옵니다.
        Long userId = authUser.id();

        // 서비스에서 페이지 단위로 데이터를 가져옵니다.
        Page<ReadBookmarkResponseDto> responsePage = bookmarkService.readAllBookmarks(userId, page , size);

        // 즐겨찾기 조회 시 200 응답 코드 리턴
        return ResponseEntity.status(HttpStatus.OK).body(responsePage);
    }

    // 로그인된 사용자의 즐겨찾기 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteBookmark(
            @Valid @RequestBody DeleteBookmarkRequestDto dto,
            @Auth AuthUser authUser
    ) {
        // 이미 인증된 사용자의 id를 가져옵니다.
        Long userId = authUser.id();

        bookmarkService.deleteBookmark(userId, dto.jobOpeningId());

        // 즐겨찾기 삭제 시 204 응답 코드 리턴
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}