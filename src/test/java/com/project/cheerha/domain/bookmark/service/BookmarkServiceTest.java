package com.project.cheerha.domain.bookmark.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.project.cheerha.domain.bookmark.dto.response.ReadBookmarkResponseDto;
import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.bookmark.repository.BookmarkRepository;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.service.JobOpeningFindByService;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.ZonedDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private UserFindByService userFindByService;

    @Mock
    private JobOpeningFindByService jobOpeningFindByService;

    @InjectMocks
    private BookmarkService bookmarkService;

    private User user;
    private JobOpening jobOpening;

    @BeforeEach
    void setUp() {
        // Given: Mock 객체들 초기화
        Long userId = 1L;
        user = User.of(
                "user2001@gmail.com",
                "김리은",
                39,
                6,
                "password123"
        );

        Long jobOpeningId = 1L;
        jobOpening = JobOpening.toEntity(
                "Software Engineer",
                "네이버",
                "서울",
                60000,
                "정규직",
                "학사",
                "http://example.com/job/1",
                0,
                3,
                "Software Engineer",
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(30),
                ZonedDateTime.now()
        );
    }

    @Test
    void 북마크_생성_성공() {
        // Given: 북마크를 생성하려는 유저와 채용공고
        Bookmark bookmark = Bookmark.toEntity(user, jobOpening);

        // When: 북마크를 저장할 때
        bookmarkService.createBookmark(user.getId(), jobOpening.getId());

        // Then: save 메서드가 1번 호출되었는지 검증
        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));  // save가 한 번 호출되었는지 확인
    }

    @Test
    void 북마크_조회_성공() {
        // Given: 북마크를 생성하려는 유저와 채용공고
        Bookmark bookmark = Bookmark.toEntity(user, jobOpening);
        List<Bookmark> bookmarks = List.of(bookmark);
        Page<Bookmark> bookmarkPage = new PageImpl<>(bookmarks);

        // When: 북마크 목록을 조회할 때
        when(bookmarkRepository.findByUserId(user.getId(), PageRequest.of(0, 10))).thenReturn(bookmarkPage);
        Page<ReadBookmarkResponseDto> result = bookmarkService.readAllBookmarks(user.getId(), PageRequest.of(0, 10));

        // Then: 조회된 북마크 목록의 size가 1이어야 하며, 회사명이 '네이버'이어야 함
        assertEquals(1, result.getTotalElements());
        assertEquals("네이버", result.getContent().get(0).company());
    }

    @Test
    void 북마크_삭제_성공() {
        // Given: 북마크를 생성하려는 유저와 채용공고
        Bookmark bookmark = Bookmark.toEntity(user, jobOpening);

        // When: 북마크 삭제를 요청할 때
        bookmarkService.deleteBookmark(user.getId(), jobOpening.getId());

        // Then: deleteByUserIdAndJobOpeningId 메서드가 1번 호출되었는지 검증
        verify(bookmarkRepository, times(1)).deleteByUserIdAndJobOpeningId(user.getId(), jobOpening.getId());
    }

    @Test
    void 북마크_최대_200개_초과시_가장_오래된_북마크_삭제() {
        // Given: 유저의 북마크가 200개일 때
        when(bookmarkRepository.existsByUserIdAndJobOpeningId(user.getId(), jobOpening.getId())).thenReturn(false);
        when(bookmarkRepository.countByUserId(user.getId())).thenReturn(200L);

        // Mock 가장 오래된 북마크 삭제
        Bookmark oldestBookmark = mock(Bookmark.class);
        when(bookmarkRepository.findFirstByUserIdOrderByIdAsc(user.getId())).thenReturn(oldestBookmark);

        // When: 새로운 북마크를 생성하려고 할 때
        bookmarkService.createBookmark(user.getId(), jobOpening.getId());

        // Then: 가장 오래된 북마크가 삭제되고 새 북마크가 저장되어야 함
        verify(bookmarkRepository, times(1)).delete(oldestBookmark);
        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
    }
}
