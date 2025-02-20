package com.project.cheerha.domain.bookmark.repository;

import com.project.cheerha.domain.bookmark.dto.request.ReadBookmarkAgeRequestDto;
import com.project.cheerha.domain.bookmark.dto.response.BookmarkCustomAgeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkRepositoryQuery {

    Page<BookmarkCustomAgeResponseDto> readTop10BookmarksByAgeGroup(ReadBookmarkAgeRequestDto requestDto, Pageable pageable);
}
