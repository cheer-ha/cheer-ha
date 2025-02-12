package com.project.cheerha.domain.bookmark.repository;

import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    // user와 dataId로 북마크 조회
    Optional<Bookmark> findByUserAndDataId(User user, Long dataId);

    // 유저 ID를 기준으로 북마크 조회 (Pageable을 사용한 페이징 처리)
    List<Bookmark> findByUser(User user, Pageable pageable);

    // user와 dataId로 북마크 삭제
    void deleteByUserAndDataId(User user, Long dataId);

}
