package com.project.cheerha.domain.bookmark.service;

import com.project.cheerha.common.exception.client.BadRequestException;
import com.project.cheerha.common.exception.client.ClientErrorCode;
import com.project.cheerha.domain.bookmark.dto.response.BookmarkCustomAgeResponseDto;
import com.project.cheerha.domain.bookmark.dto.response.ReadBookmarkResponseDto;
import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.bookmark.repository.BookmarkRepository;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.service.JobOpeningFindByService;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    private static final int MAX_BOOKMARK_COUNT = 200;
    private static final int MAX_PAGES = 20;

    /**
     * 사용자가 채용 공고를 북마크하는 메서드입니다.
     *
     * @param userId 사용자의 ID
     * @param jobOpeningId 채용 공고의 ID
     */
    @Transactional
    @CacheEvict(value = "bookmarks", key = "#userId")
    public void createBookmark(Long userId, Long jobOpeningId) {
        JobOpening jobOpening = jobOpeningFindByService.findById(jobOpeningId);
        boolean isBookmarkExists = bookmarkRepository.existsByUserIdAndJobOpeningId(userId, jobOpeningId);
        if (isBookmarkExists) {
            return;
        }
        long bookmarkCount = bookmarkRepository.countByUserId(userId);
        if (bookmarkCount >= MAX_BOOKMARK_COUNT) {
            Bookmark oldestBookmark = bookmarkRepository.findFirstByUserIdOrderByIdAsc(userId);
            bookmarkRepository.delete(oldestBookmark);
        }
        User user = userFindByIdService.findById(userId);
        Bookmark bookmark = Bookmark.toEntity(user, jobOpening);
        bookmarkRepository.save(bookmark);
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
        // 페이지 번호가 20을 초과하면 20으로 설정
        int pageNumber = pageable.getPageNumber();
        if (pageNumber >= MAX_PAGES) {
            pageNumber = MAX_PAGES - 1; // 20페이지를 넘지 않도록 제한
        }
        Pageable limitedPageable = PageRequest.of(pageNumber, pageable.getPageSize());
        Page<Bookmark> bookmarkPage = bookmarkRepository.findByUserId(userId, limitedPageable);
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
    }

    /**
     * 커스텀 연령대 즐겨찾기 로직 중, 나이에 대한 예외처리가 진행됩니다.

     * @return 커스텀 연령대 즐겨찾기 상위 10개 리스트
     */

    public List<BookmarkCustomAgeResponseDto> readTop10BookmarkByAgeBetween (int minAge, int maxAge) {

         // 최소나이가 최대나이보다 클 때 예외처리하는 로직 추가
        if (minAge > maxAge) {
            throw new BadRequestException(ClientErrorCode.MIN_AGE_EXCEEDS_MAX_AGE);
        }
       return bookmarkRepository.readTop10BookmarksByAgeBetween(minAge, maxAge);
    }
}
