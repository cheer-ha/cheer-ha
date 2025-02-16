package com.project.cheerha.domain.bookmark.service;

import com.project.cheerha.domain.bookmark.dto.response.ReadBookmarkResponseDto;
import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.bookmark.repository.BookmarkRepository;
import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import com.project.cheerha.domain.jobOpening.service.JobOpeningFindByService;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserFindByService userFindByIdService;
    private final JobOpeningFindByService jobOpeningFindByService;

    private static final int MAX_BOOKMARK_COUNT = 200;

    /**
     * 사용자가 채용 공고를 북마크하는 메서드입니다.
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

        // 사용자의 북마크 개수를 체크하고, 200개 이상이면 가장 오래된 북마크 삭제
        long bookmarkCount = bookmarkRepository.countByUserId(userId);
        if (bookmarkCount >= MAX_BOOKMARK_COUNT) {
            // 가장 오래된 북마크 삭제 (최신 순으로 정렬 후 첫 번째 항목 삭제)
            Bookmark oldestBookmark = bookmarkRepository.findFirstByUserIdOrderByIdAsc(userId);
            bookmarkRepository.delete(oldestBookmark);
            log.info("Deleted oldest bookmark to maintain limit of {} bookmarks", MAX_BOOKMARK_COUNT);
        }

        // 북마크 저장
        Bookmark bookmark = Bookmark.toEntity(user, jobOpening);
        bookmarkRepository.save(bookmark);

        // 북마크를 Redis 캐시에서 제거 (갱신하기 위해서)
        // 캐시에서 해당 userId에 대한 북마크를 갱신
        evictBookmarkCache(userId);
    }

    /**
     * 사용자의 모든 북마크를 페이징 처리하여 조회하는 메서드입니다.
     *
     * @param userId 사용자의 ID
     * @param pageable 페이지 번호와 크기를 포함한 Pageable 객체
     * @return 페이징된 북마크 목록 (ReadBookmarkResponseDto 형태)
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "bookmarks", key = "#userId", unless = "#result.isEmpty()")
    public Page<ReadBookmarkResponseDto> readAllBookmarks(Long userId, Pageable pageable) {
        long startTime = System.currentTimeMillis();
        Page<Bookmark> bookmarkPage = bookmarkRepository.findByUserId(userId, pageable);
        long endTime = System.currentTimeMillis();
        log.info("Execution Time for readAllBookmarks: {} ms", (endTime - startTime));
        return bookmarkPage.map(ReadBookmarkResponseDto::toDto);
    }

    /**
     * 사용자가 저장한 채용 공고의 북마크를 삭제하는 메서드입니다.
     *
     * @param userId 사용자의 ID
     * @param jobOpeningId 삭제할 채용 공고의 ID
     */
    @Transactional
    @CacheEvict(value = "bookmarks", key = "#userId")
    public void deleteBookmark(Long userId, Long jobOpeningId) {
        bookmarkRepository.deleteByUserIdAndJobOpeningId(userId, jobOpeningId);
        evictBookmarkCache(userId);
    }

    /**
     * 캐시에서 해당 사용자 북마크를 갱신하는 메서드입니다.
     *
     * @param userId 사용자의 ID
     */
    private void evictBookmarkCache(Long userId) {
        log.info("Evicting bookmark cache for userId: {}", userId);
    }
}
