package com.project.cheerha.common.redis.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cheerha.domain.bookmark.dto.response.ReadBookmarkResponseDto;
import com.project.cheerha.domain.bookmark.entity.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisBookmarkService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    // 캐시에서 북마크를 가져오는 메서드
    public List<ReadBookmarkResponseDto> getAllBookmarksFromCache(Long userId) {
        Set<Object> cachedBookmarkIdsSet = redisTemplate.opsForHash().keys("user:" + userId + ":bookmarks");
        List<Object> cachedBookmarkObjects = new ArrayList<>(cachedBookmarkIdsSet);

        if (!cachedBookmarkObjects.isEmpty()) {
            List<Object> rawCacheData = redisTemplate.opsForHash().multiGet("user:" + userId + ":bookmarks", cachedBookmarkObjects);
            List<ReadBookmarkResponseDto> dtoList = new ArrayList<>();

            for (Object rawData : rawCacheData) {
                if (rawData instanceof LinkedHashMap) {
                    //json -> dto
                    ReadBookmarkResponseDto dto = objectMapper.convertValue(rawData, ReadBookmarkResponseDto.class);
                    dtoList.add(dto);
                } else if (rawData instanceof ReadBookmarkResponseDto) {
                    dtoList.add((ReadBookmarkResponseDto) rawData);
                }
            }
            return dtoList;
        }
        return new ArrayList<>();
    }

    // 북마크 추가 후 캐시 갱신하는 메서드
    public void updateCacheOnBookmarkAdd(Long userId, Bookmark bookmark) {
        ReadBookmarkResponseDto dto = ReadBookmarkResponseDto.toDto(bookmark);
        redisTemplate.opsForHash().put("user:" + userId.toString() + ":bookmarks", bookmark.getJobOpening().getId().toString(), dto);
    }

    // 캐시에서 북마크 삭제하는 메서드
    public void deleteBookmarkFromCache(Long userId, Long jobOpeningId) {
        redisTemplate.opsForHash().delete("user:" + userId + ":bookmarks", jobOpeningId);
    }
}
