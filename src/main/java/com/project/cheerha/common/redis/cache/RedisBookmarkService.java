package com.project.cheerha.common.redis.cache;

import com.project.cheerha.domain.bookmark.entity.Bookmark;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RedisBookmarkService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisBookmarkService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 캐시에서 북마크를 가져오는 메서드
    public List<Object> getAllBookmarksFromCache(Long userId) {
        Set<Object> cachedBookmarkIdsSet = redisTemplate.opsForHash().keys("user:" + userId + ":bookmarks");
        List<Object> cachedBookmarkIds = new ArrayList<>(cachedBookmarkIdsSet);
        if (!cachedBookmarkIds.isEmpty()) {
            return redisTemplate.opsForHash().multiGet("user:" + userId + ":bookmarks", cachedBookmarkIds);
        }
        return new ArrayList<>();
    }

    // 북마크 추가 후 캐시 갱신하는 메서드
    public void updateCacheOnBookmarkAdd(Long userId, Bookmark bookmark) {
        redisTemplate.opsForHash().put("user:" + userId + ":bookmarks", bookmark.getJobOpening().getId(), bookmark);
    }

    // 캐시에서 북마크 삭제하는 메서드
    public void deleteBookmarkFromCache(Long userId, Long jobOpeningId) {
        redisTemplate.opsForHash().delete("user:" + userId + ":bookmarks", jobOpeningId);
    }
}
