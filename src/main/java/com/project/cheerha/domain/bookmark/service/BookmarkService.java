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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserFindByService userFindByIdService;
    private final JobOpeningFindByService jobOpeningFindByService;
    private final RedisTemplate<String, Long> redisTemplate;

    private static final int MAX_BOOKMARK_COUNT = 200;
    private static final int MAX_PAGES = 20;

    /**
     * 사용자가 채용 공고를 북마크하는 메서드입니다.
     *
     * 이 메서드는 사용자가 북마크할 채용 공고를 추가하며,
     * 사용자가 이미 북마크한 채용 공고라면 추가하지 않습니다.
     * 또한, 사용자가 저장할 수 있는 북마크의 개수가 최대값을 초과하는 경우,
     * 가장 오래된 북마크를 삭제하여 새로운 북마크를 추가합니다.
     * 그리고 Redis 캐시를 업데이트합니다.
     *
     * @param userId 사용자의 ID
     * @param jobOpeningId 채용 공고의 ID
     */
    @Transactional
    public void createBookmark(Long userId, Long jobOpeningId) {
        JobOpening jobOpening = jobOpeningFindByService.findById(jobOpeningId);
        boolean isBookmarkExists = bookmarkRepository.existsByUserIdAndJobOpeningId(userId, jobOpeningId);

        // 이미 북마크된 채용공고라면 추가하지 않음
        if (isBookmarkExists) {
            return;
        }

        // 사용자의 북마크가 최대 개수를 초과하면, 가장 오래된 북마크를 삭제
        long bookmarkCount = bookmarkRepository.countByUserId(userId);
        if (bookmarkCount >= MAX_BOOKMARK_COUNT) {
            Bookmark oldestBookmark = bookmarkRepository.findFirstByUserIdOrderByIdAsc(userId);
            bookmarkRepository.delete(oldestBookmark);
        }

        // 새로운 북마크를 추가
        User user = userFindByIdService.findById(userId);
        Bookmark bookmark = Bookmark.toEntity(user, jobOpening);
        bookmarkRepository.save(bookmark);

        // Redis 캐시 업데이트 (북마크가 추가되었을 때 캐시 갱신)
        redisTemplate.opsForList().leftPush("user:" + userId + ":bookmarks", jobOpeningId);
    }

    /**
     * 사용자의 모든 북마크를 페이징 처리하여 조회하는 메서드입니다.
     *
     * 이 메서드는 사용자의 북마크 목록을 페이징 처리하여 조회합니다.
     * 캐시에서 북마크 ID를 먼저 가져와서, 캐시가 있는 경우 이를 사용하여 DB에서 조회하고,
     * 캐시가 비어있을 경우 DB에서 직접 조회합니다.
     * 페이지 번호가 최대 페이지 번호(20)를 넘지 않도록 설정됩니다.
     *
     * @param userId 사용자의 ID
     * @param pageable 페이지 번호와 크기를 포함한 Pageable 객체
     * @return 페이징된 북마크 목록 (ReadBookmarkResponseDto 형태)
     */
    @Transactional(readOnly = true)
    public Page<ReadBookmarkResponseDto> readAllBookmarks(Long userId, Pageable pageable) {
        // 페이지 번호가 20을 초과하면 20으로 설정
        int pageNumber = pageable.getPageNumber();
        if (pageNumber >= MAX_PAGES) {
            pageNumber = MAX_PAGES - 1; // 20페이지를 넘지 않도록 제한
        }
        Pageable limitedPageable = PageRequest.of(pageNumber, pageable.getPageSize());

        // 캐시에서 데이터를 가져오는 경우
        List<Long> cachedBookmarks = redisTemplate.opsForList().range("user:" + userId + ":bookmarks", 0, -1);

        // 캐시에서 가져온 북마크들의 ID에 해당하는 데이터를 DB에서 페이징 처리하여 조회
        if (!cachedBookmarks.isEmpty()) {
            Page<Bookmark> bookmarkPage = bookmarkRepository.findByUserIdAndJobOpeningIdIn(userId, cachedBookmarks, limitedPageable);
            return bookmarkPage.map(ReadBookmarkResponseDto::toDto);
        } else {
            // 캐시가 비어있으면 DB에서 조회
            Page<Bookmark> bookmarkPage = bookmarkRepository.findByUserId(userId, limitedPageable);
            return bookmarkPage.map(ReadBookmarkResponseDto::toDto);
        }
    }

    /**
     * 사용자가 저장한 채용 공고의 북마크를 삭제하는 메서드입니다.
     *
     * 이 메서드는 사용자가 저장한 특정 채용 공고의 북마크를 삭제하며,
     * 삭제된 후 Redis에서 해당 북마크를 제거합니다.
     *
     * @param userId 사용자의 ID
     * @param jobOpeningId 삭제할 채용 공고의 ID
     */
    @Transactional
    public void deleteBookmark(Long userId, Long jobOpeningId) {
        bookmarkRepository.deleteByUserIdAndJobOpeningId(userId, jobOpeningId);

        // Redis에서 북마크 삭제
        redisTemplate.opsForList().remove("user:" + userId + ":bookmarks", 1, jobOpeningId);
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
