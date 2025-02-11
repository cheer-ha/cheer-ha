package com.project.cheerha.domain.bookmark.controller;

import com.project.cheerha.common.annotation.Auth;
import com.project.cheerha.common.dto.AuthUser;
import com.project.cheerha.domain.bookmark.dto.ReadBookmarkResponseDto;
import com.project.cheerha.domain.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 로그인된 사용자의 즐겨찾기 추가
    @PostMapping("/{dataId}")
    public ResponseEntity<Void> createBookmark(
            @PathVariable Long dataId,  // dataId는 URL에서 받음
            @Auth AuthUser authUser     // userId는 로그인된 유저에서 받음
    ) {
        bookmarkService.createBookmark(authUser, dataId);

        // 즐겨찾기 추가 시 201 응답 코드 리턴
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 로그인된 사용자의 모든 즐겨찾기 조회
    @GetMapping
    public ResponseEntity<List<ReadBookmarkResponseDto>> readBookmarkList(
            @Auth AuthUser authUser
    ) {
        List<ReadBookmarkResponseDto> response = bookmarkService.readBookmarkList(authUser);

        // 즐겨찾기 조회 시 200 응답 코드 리턴
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 로그인된 사용자의 즐겨찾기 삭제
    @DeleteMapping("/{dataId}")
    public ResponseEntity<Void> deleteBookmark(
            @PathVariable Long dataId,
            @Auth AuthUser authUser
    ) {
        bookmarkService.deleteBookmark(authUser, dataId);

        // 즐겨찾기 삭제 시 204 응답 코드 리턴
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
