package com.project.cheerha.domain.bookmark.repository;

import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.data.entity.Data;
import com.project.cheerha.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepositoryQuery {

    // user_id와 data_id로 즐겨찾기 찾기
    Optional<Bookmark> findByUserAndData(User user, Data data);

    // 로그인한 사용자의 모든 즐겨찾기 조회
    List<Bookmark> findAllByUser(User user);
}
