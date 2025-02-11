package com.project.cheerha.domain.bookmark.dto;

import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.data.entity.Data;
import com.project.cheerha.domain.user.entity.User;

public record CreateBookmarkRequestDto(
        Long dataId
) {
    // userId로 User를 찾아서 Bookmark 엔티티 생성
    public Bookmark toEntity(User user, Data data) {
        return new Bookmark(user, data);  // user와 data를 이용해 Bookmark 엔티티 생성
    }
}