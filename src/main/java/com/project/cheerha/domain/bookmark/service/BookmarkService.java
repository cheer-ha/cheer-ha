package com.project.cheerha.domain.bookmark.service;

import com.project.cheerha.common.exception.CustomException;
import com.project.cheerha.common.exception.ErrorCode;
import com.project.cheerha.domain.bookmark.dto.ReadBookmarkResponseDto;
import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.bookmark.repository.BookmarkRepository;
import com.project.cheerha.domain.data.entity.Data;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.repository.UserRepository;
import com.project.cheerha.domain.data.repository.DataRepository;
import com.project.cheerha.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final DataRepository dataRepository;

    // 로그인된 사용자의 즐겨찾기 추가
    @Transactional
    public void createBookmark(AuthUser authUser, Long dataId) {
        // AuthUser로 유저 검증
        User user = userRepository.findById(authUser.id())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 데이터 조회
        Data data = dataRepository.findById(dataId)
                .orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));

        // 북마크 생성 후 저장
        Bookmark bookmark = new Bookmark(user, data);
        bookmarkRepository.save(bookmark);
    }

    // 로그인된 사용자의 모든 즐겨찾기 조회
    public List<ReadBookmarkResponseDto> readBookmarkList(AuthUser authUser) {
        // AuthUser로 유저 조회
        User user = userRepository.findById(authUser.id())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 해당 사용자의 북마크 목록 조회
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);
        return bookmarks.stream()
                .map(ReadBookmarkResponseDto::fromEntity)
                .toList();
    }

    // 로그인된 사용자의 즐겨찾기 삭제
    @Transactional
    public void deleteBookmark(AuthUser authUser, Long dataId) {
        // AuthUser로 유저 조회
        User user = userRepository.findById(authUser.id())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 데이터 조회
        Data data = dataRepository.findById(dataId)
                .orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));

        // 북마크 조회
        Bookmark bookmark = bookmarkRepository.findByUserAndData(user, data)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));

        // 북마크 삭제
        bookmarkRepository.delete(bookmark);
    }
}
