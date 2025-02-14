package com.project.cheerha.domain.bookmark.service;

import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.domain.bookmark.dto.response.ReadBookmarkResponseDto;
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

    /**
     * 사용자가 채용 공고를 북마크하는 메서드입니다.
     *
     * 주어진 사용자 ID와 채용 공고 ID를 기반으로 북마크를 생성합니다. 만약 이미 해당 사용자가
     * 해당 채용 공고를 북마크한 경우, 아무 작업도 하지 않고 종료됩니다.
     *
     * @param userId 사용자의 ID
     * @param jobOpeningId 채용 공고의 ID
     */
    @Transactional
    public void createBookmark(Long userId, Long jobOpeningId) {
        JobOpening jobOpening = getJobOpeningById(jobOpeningId);
        User user = getUserById(userId);
        boolean bookmarkExists = bookmarkRepository.existsByUserIdAndJobOpeningId(userId, jobOpeningId);
        if (bookmarkExists) {
            return;
        }
        Bookmark bookmark = Bookmark.toEntity(user, jobOpening);
        bookmarkRepository.save(bookmark);
    }

    /**
     * 사용자의 모든 북마크를 조회하는 메서드입니다.
     *
     * 주어진 사용자 ID를 기반으로 사용자의 모든 북마크를 페이징 처리하여 반환합니다.
     * 페이지나 페이지크기가 1이하일 경우 1로 처리합니다.
     *
     * @param userId 사용자의 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 북마크 목록 (ReadBookmarkResponseDto 형태)
     */
    @Transactional(readOnly = true)
    public Page<ReadBookmarkResponseDto> readAllBookmarks(Long userId, int page, int size) {
        int correctedPage = Math.max(page - 1, 0);
        int correctedSize = Math.max(size, 1);
        Pageable pageable = PageRequest.of(correctedPage,correctedSize);
        Page<Bookmark> bookmarkPage = bookmarkRepository.findByUserId(userId, pageable);
        return bookmarkPage.map(ReadBookmarkResponseDto::toDto);
    }

    /**
     * 사용자가 저장한 채용 공고의 북마크를 삭제하는 메서드입니다.
     *
     * 주어진 사용자 ID와 채용 공고 ID를 기반으로 해당 북마크를 삭제합니다.
     *
     * @param userId 사용자의 ID
     * @param jobOpeningId 삭제할 채용 공고의 ID
     */
    @Transactional
    public void deleteBookmark(Long userId, Long jobOpeningId) {
        // userId와 jobOpeningId로 북마크 삭제
        bookmarkRepository.deleteByUserIdAndJobOpeningId(userId, jobOpeningId);
    }

    /**
     * 사용자 정보를 조회하는 메서드입니다.
     *
     * 주어진 userId에 해당하는 사용자를 조회합니다. 사용자가 존재하지 않으면 예외를 던집니다.
     *
     * @param userId 사용자의 ID
     * @return 조회된 사용자 객체
     * @throws NotFoundException 사용자가 존재하지 않을 경우
     */
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(DataErrorCode.USER_NOT_FOUND));
    }

    /**
     * 채용 공고 정보를 조회하는 메서드입니다.
     *
     * 주어진 jobOpeningId에 해당하는 채용 공고를 조회합니다. 채용 공고가 존재하지 않으면 예외를 던집니다.
     *
     * @param jobOpeningId 채용 공고의 ID
     * @return 조회된 채용 공고 객체
     * @throws NotFoundException 채용 공고가 존재하지 않을 경우
     */
    private JobOpening getJobOpeningById(Long jobOpeningId) {
        return jobOpeningRepository.findById(jobOpeningId)
                .orElseThrow(() -> new NotFoundException(DataErrorCode.JOB_OPENING_NOT_FOUND));
    }
}
