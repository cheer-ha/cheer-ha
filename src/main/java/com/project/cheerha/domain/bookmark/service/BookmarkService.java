package com.project.cheerha.domain.bookmark.service;

import com.project.cheerha.domain.bookmark.dto.response.ReadBookmarkResponseDto;
import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.bookmark.repository.BookmarkRepository;
import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import com.project.cheerha.domain.jobOpening.service.JobOpeningFindByService;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
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
    private final UserFindByService userFindByIdService;
    private final JobOpeningFindByService jobOpeningFindByService;

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
        // 데이터 조회: 주어진 jobOpeningId에 해당하는 JobOpening을 조회
        JobOpening jobOpening = jobOpeningFindByService.findById(jobOpeningId);

        // userId로 User 객체를 조회
        User user = userFindByIdService.findById(userId);

        // 이미 존재하는 북마크가 있는지 확인 (userId로 조회) - exists 사용
        boolean isBookmarkExists = bookmarkRepository.existsByUserIdAndJobOpeningId(userId, jobOpeningId);
        if (isBookmarkExists) {
            return;
        }
        Bookmark bookmark = Bookmark.toEntity(user, jobOpening);
        bookmarkRepository.save(bookmark);
    }

    /**
     * 사용자의 모든 북마크를 페이징 처리하여 조회하는 메서드입니다.
     *
     * 주어진 사용자 ID를 기반으로 사용자의 모든 북마크를 페이징 처리하여 반환합니다.
     * 페이지 번호와 크기는 `Pageable` 객체를 통해 전달되며, 페이지 번호나 크기가 1 이하일 경우 `validatePageSize` 메서드에서 1로 처리됩니다.
     *
     * @param userId 사용자의 ID
     * @param pageable 페이지 번호와 크기를 포함한 Pageable 객체
     * @return 페이징된 북마크 목록 (ReadBookmarkResponseDto 형태)
     */
    @Transactional(readOnly = true)
    public Page<ReadBookmarkResponseDto> readAllBookmarks(Long userId, Pageable pageable) {
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

}
