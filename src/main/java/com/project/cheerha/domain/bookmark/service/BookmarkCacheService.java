package com.project.cheerha.domain.bookmark.service;

import com.project.cheerha.common.repository.KeyHashRepository;
import com.project.cheerha.domain.bookmark.entity.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BookmarkCacheService {

    private final KeyHashRepository keyHashRepository;

    // 캐시에서 북마크를 가져오는 메서드
    public List<Object> getAllBookmarksFromCache(Long userId) {
        Set<Object> cachedBookmarkIdsSet = keyHashRepository.getKeys("user:" + userId + ":bookmarks");
        List<Object> cachedBookmarkIds = new ArrayList<>(cachedBookmarkIdsSet);
        if (!cachedBookmarkIds.isEmpty()) {
            return keyHashRepository.multiGet("user:" + userId + ":bookmarks", cachedBookmarkIds);
        }
        return new ArrayList<>();
    }

    // 북마크 추가 후 캐시 갱신하는 메서드
    public void updateCacheOnBookmarkAdd(Long userId, Bookmark bookmark) {
        keyHashRepository.putValue("user:" + userId + ":bookmarks", bookmark.getJobOpening().getId(), bookmark);
    }

    // 캐시에서 북마크 삭제하는 메서드
    public void deleteBookmarkFromCache(Long userId, Long jobOpeningId) {
        keyHashRepository.deleteValue("user:" + userId + ":bookmarks", jobOpeningId);
    }
}
