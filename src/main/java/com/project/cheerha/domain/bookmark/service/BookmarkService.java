package com.project.cheerha.domain.bookmark.service;

import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.domain.bookmark.dto.ReadBookmarkResponseDto;
import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.bookmark.repository.BookmarkRepository;
import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import com.project.cheerha.domain.jobOpening.repository.JobOpeningRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final JobOpeningRepository jobOpeningRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createBookmark(Long userId, Long jobOpeningId) {
        // 데이터 조회: 주어진 jobOpeningId에 해당하는 JobOpening을 조회
        JobOpening jobOpening = getJobOpeningById(jobOpeningId);

        // userId로 User 객체를 조회
        User user = getUserById(userId);

        // 이미 존재하는 북마크가 있는지 확인 (userId로 조회) - exists 사용
        boolean bookmarkExists = bookmarkRepository.existsByUserIdAndJobOpeningId(userId, jobOpeningId);
        if (bookmarkExists) {
            // 이미 존재하면 그냥 리턴
            return;
        }

        // Bookmark 엔티티 생성 (toEntity 메서드 사용)
        Bookmark bookmark = Bookmark.toEntity(user, jobOpening);
        bookmarkRepository.save(bookmark);
    }

    // 로그인된 사용자의 모든 즐겨찾기 조회 (페이징 처리)
    @Transactional(readOnly = true)
    public Page<ReadBookmarkResponseDto> readAllBookmarks(Long userId, int page, int size) {
        // 페이지가 1 이하일 경우 0으로 처리 (0보다 작은 값은 0으로, 1 이상은 그대로)
        int correctedPage = Math.max(page - 1, 0);

        // 페이지네이션 처리
        Pageable pageable = PageRequest.of(correctedPage, size);

        // userId로 북마크 목록을 가져옴
        Page<Bookmark> bookmarkPage = bookmarkRepository.findByUserId(userId, pageable);

        // Page<Bookmark>를 Page<ReadBookmarkResponseDto>로 변환
        return bookmarkPage.map(ReadBookmarkResponseDto::toDto);
    }

    // 로그인된 사용자의 즐겨찾기 삭제
    @Transactional
    public void deleteBookmark(Long userId, Long jobOpeningId) {
        // userId와 jobOpeningId로 북마크 삭제
        bookmarkRepository.deleteByUserIdAndJobOpeningId(userId, jobOpeningId);
    }

    // 유저 정보 조회를 위한 private 메서드
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(DataErrorCode.USER_NOT_FOUND));
    }

    // 채용공고 조회를 위한 private 메서드
    private JobOpening getJobOpeningById(Long jobOpeningId) {
        return jobOpeningRepository.findById(jobOpeningId)
                .orElseThrow(() -> new NotFoundException(DataErrorCode.JOB_OPENING_NOT_FOUND));
    }
}
