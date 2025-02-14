package com.project.cheerha.domain.bookmark.repository;

import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // userId와 jobOpeningId로 북마크 조회
    boolean existsByUserIdAndJobOpeningId(Long userId, Long jobOpeningId);

    // userId를 기준으로 북마크 조회 (Pageable을 사용한 페이징 처리)
    Page<Bookmark> findByUserId(Long userId, Pageable pageable);

    // userId와 jobOpeningId로 북마크 삭제
    void deleteByUserIdAndJobOpeningId(Long userId, Long jobOpeningId);
}
