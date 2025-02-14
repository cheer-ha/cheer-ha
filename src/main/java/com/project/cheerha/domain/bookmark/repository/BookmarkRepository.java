package com.project.cheerha.domain.bookmark.repository;

import com.project.cheerha.domain.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    /**
     * 사용자 ID와 채용 공고 ID를 기준으로 북마크가 존재하는지 확인하는 메서드입니다.
     *
     * 주어진 `userId`와 `jobOpeningId`에 해당하는 북마크가 이미 존재하는지 여부를 반환합니다.
     *
     * @param userId 사용자의 ID
     * @param jobOpeningId 채용 공고의 ID
     * @return 해당 사용자와 채용 공고에 대한 북마크가 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByUserIdAndJobOpeningId(Long userId, Long jobOpeningId);

    /**
     * 사용자 ID를 기준으로 북마크를 조회하는 메서드입니다.
     *
     * 주어진 `userId`에 해당하는 사용자의 북마크 목록을 페이징 처리하여 반환합니다.
     *
     * @param userId 사용자의 ID
     * @param pageable 페이징 정보 (페이지 번호와 크기)
     * @return 주어진 사용자에 대한 북마크 목록 (페이징 처리된 결과)
     */
    Page<Bookmark> findByUserId(Long userId, Pageable pageable);

    /**
     * 사용자 ID와 채용 공고 ID를 기준으로 북마크를 삭제하는 메서드입니다.
     *
     * 주어진 `userId`와 `jobOpeningId`에 해당하는 북마크를 삭제합니다.
     *
     * @param userId 사용자의 ID
     * @param jobOpeningId 채용 공고의 ID
     */
    void deleteByUserIdAndJobOpeningId(Long userId, Long jobOpeningId);
}
