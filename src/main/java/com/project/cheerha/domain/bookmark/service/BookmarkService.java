package com.project.cheerha.domain.bookmark.service;

import com.project.cheerha.common.exception.CustomException;
import com.project.cheerha.common.exception.ErrorCode;
import com.project.cheerha.domain.bookmark.dto.ReadBookmarkResponseDto;
import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.bookmark.repository.BookmarkRepository;
import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import com.project.cheerha.domain.jobOpening.repository.JobOpeningRepository;
import com.project.cheerha.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final JobOpeningRepository jobOpeningRepository;
    @Transactional
    public void createBookmark(User user, Long jobOpeningId) {
        // 데이터 조회: 주어진 jobOpeningId에 해당하는 JobOpening을 조회
        JobOpening jobOpening = getJobOpeningById(jobOpeningId);

        // 이미 존재하는 북마크가 있는지 확인
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserAndJobOpeningId(user, jobOpeningId);  // 수정된 부분
        if (existingBookmark.isPresent()) {
            // 이미 존재하면 그냥 리턴
            return;
        }

        // 북마크 생성 후 저장
        Bookmark bookmark = new Bookmark(user, jobOpening);
        bookmarkRepository.save(bookmark);
    }

    // 로그인된 사용자의 모든 즐겨찾기 조회 (페이징 처리)
    @Transactional
    public List<ReadBookmarkResponseDto> readBookmarkList(User user, int page, int size) {
        // 북마크 목록을 가져올 때 페이지네이션 처리
        Pageable pageable = PageRequest.of(page, size);
        List<Bookmark> bookmarks = bookmarkRepository.findByUser(user, pageable);

        // Bookmark -> ReadBookmarkResponseDto로 변환
        return bookmarks.stream()
                .map(ReadBookmarkResponseDto::toDto)
                .collect(Collectors.toList());
    }

    // 로그인된 사용자의 즐겨찾기 삭제
    @Transactional
    public void deleteBookmark(User user, Long jobOpeningId) {
        // userId와 jobOpeningId로 북마크 삭제하고 성공 여부 확인
        bookmarkRepository.deleteByUserAndJobOpeningId(user, jobOpeningId);  // 수정된 부분
    }

    // 채용공고 조회를 위한 private 메서드
    private JobOpening getJobOpeningById(Long jobOpeningId) {
        return jobOpeningRepository.findById(jobOpeningId)
                .orElseThrow(() -> new CustomException(ErrorCode.JOB_OPENING_NOT_FOUND));
    }
}
